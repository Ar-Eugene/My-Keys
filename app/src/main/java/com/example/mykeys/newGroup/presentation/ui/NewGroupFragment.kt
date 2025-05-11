package com.example.mykeys.newGroup.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mykeys.R
import com.example.mykeys.databinding.FragmentNewGroupBinding
import com.example.mykeys.newGroup.presentation.viewmodel.NewGroupViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewGroupFragment : Fragment() {

    private var _binding: FragmentNewGroupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewGroupViewModel by viewModels()
    private val args: NewGroupFragmentArgs by navArgs()

    // Регистрация лаунчера для выбора изображения
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                binding.cover.setImageURI(it)
                viewModel.setImageGroup(it)
                try {
                    requireContext().contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) {
                    Log.e("NewGroupFragment", "Не удалось сохранить разрешение на URI: $it", e)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Проверяем, находимся ли мы в режиме редактирования
        args.groupModel?.let { groupModel ->
            viewModel.loadGroupForEdit(groupModel)
        }
        observeViewModel()
        clickListener()
        setupAllTextListeners()
    }

    // хранятся все clickListener
    private fun clickListener() {
        backArrowNavigationToMainFragment()
        btnApplyAction()
        setupCoverClickListener()
    }

    // наблюдаем за за данными ViewModel
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.selectedImageUri.collect { uri ->
                        uri?.let {
                            binding.cover.setImageURI(it)
                            binding.cover.background = null
                        }
                    }
                }

                launch {
                    viewModel.isEditMode.collect { isEditMode ->
                        // Меняем текст кнопки в зависимости от режима
                        binding.btnApply.text = if (isEditMode)
                            getString(R.string.save) else getString(R.string.create)
                    }
                }

                // Заполняем поля данными из ViewModel
                launch {
                    viewModel.groupName.collect { name ->
                        if (binding.nameCategory.editText?.text.toString() != name) {
                            binding.nameCategory.editText?.setText(name)
                        }
                    }
                }

                launch {
                    viewModel.emailName.collect { email ->
                        if (binding.txtEmail.editText?.text.toString() != email) {
                            binding.txtEmail.editText?.setText(email)
                        }
                    }
                }

                launch {
                    viewModel.passwordName.collect { password ->
                        if (binding.txtPassword.editText?.text.toString() != password) {
                            binding.txtPassword.editText?.setText(password)
                        }
                    }
                }

                launch {
                    viewModel.loginName.collect { login ->
                        if (binding.txtLogin.editText?.text.toString() != login) {
                            binding.txtLogin.editText?.setText(login)
                        }
                    }
                }
            }
        }
    }

    // перехода на MainFragment через backArrow
    private fun backArrowNavigationToMainFragment() {
        binding.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    // перехода на MainFragment через btnApply и создание группы и тут же возврат на DescriptionCategoryFragment
    private fun btnApplyAction() {
        binding.btnApply.setOnClickListener {
            val name = (binding.nameCategory.editText as? TextInputEditText)?.text.toString()
            val email = (binding.txtEmail.editText as? TextInputEditText)?.text.toString()

            // Проверяем имя категории
            if (name.isBlank()) {
                Toast.makeText(requireContext(), "Введите название категории", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Проверяем email, если он не пустой
            if (email.isNotEmpty() && !validateEmail(email)) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_email2),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Если все проверки пройдены, создаем или сохраняем группу
            lifecycleScope.launch {
                val updatedGroup = viewModel.createOrSaveGroup()
                if (updatedGroup == null) {
                    // Если группа не создана из-за дублирования имени
                    Toast.makeText(
                        requireContext(),
                        "Группа с таким именем уже существует",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }
                
                if (viewModel.isEditMode.value) {
                    // Если в режиме редактирования, возвращаемся на экран описания
                    findNavController().navigate(
                        NewGroupFragmentDirections.actionNewGroupFragmentToDescriptionCategoryFragment(
                            updatedGroup
                        )
                    )
                } else {
                    // Если в режиме создания, возвращаемся на главный экран
                    findNavController().navigateUp()
                }
            }
        }
    }

    // Слушатель для выбора изображения
    private fun setupCoverClickListener() {
        binding.cover.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }
    }

    // Метод для настройки всех текстовых полей
    private fun setupAllTextListeners() {
        setupTextChangeListener(binding.nameCategory) { text ->
            viewModel.setGroupName(text)
        }

        setupTextChangeListener(binding.txtEmail) { text ->
            viewModel.setEmailName(text)
            validateEmail(text)
        }

        setupTextChangeListener(binding.txtPassword) { text ->
            viewModel.setPasswordName(text)
        }

        setupTextChangeListener(binding.txtLogin) { text ->
            viewModel.setLoginName(text)
        }
    }

    // проверяем, чтобы email отвечал требованиям формата email
    private fun validateEmail(email: String): Boolean {
        val emailPattern = getString(R.string.emailPattern)
        val isValid = email.matches(emailPattern.toRegex())

        if (email.isNotEmpty() && !isValid) {
            binding.txtEmail.error = getString(R.string.error_email)
            binding.txtEmail.setErrorTextColor(
                resources.getColorStateList(
                    R.color.delete_group_color,
                    null
                )
            )
            return false
        } else {
            binding.txtEmail.error = null
            return true
        }
    }

    // Универсальный метод для установки TextWatcher на любое поле
    private fun setupTextChangeListener(
        textInputLayout: TextInputLayout,
        onTextChanged: (String) -> Unit
    ) {
        textInputLayout.editText?.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                onTextChanged(text.toString())
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetState() // Сбрасываем состояние при уничтожении вида
        _binding = null
    }

}