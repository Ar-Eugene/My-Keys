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
import com.example.mykeys.databinding.FragmentNewGroupBinding
import com.example.mykeys.newGroup.presentation.viewmodel.NewGroupViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewGroupFragment : Fragment() {

    private var _binding: FragmentNewGroupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewGroupViewModel by viewModels()

    // Регистрация лаунчера для выбора изображения
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                binding.cover.setImageURI(uri)
                viewModel.setImageGroup(uri)

                // Сохранить разрешения для URI
                try {
                    requireContext().contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                } catch (e: SecurityException) {
                    Log.e(
                        "NewGroupFragment",
                        "Не удалось получить постоянное разрешение для URI: $uri",
                        e
                    )
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

        observereateGroupViewModel()
        clickListener()
        setupNameTextChangeListener()

    }

    // хранятся все clickListener
    private fun clickListener() {
        backArrowNavigationToMainFragment()
        btnApplyNavigationToMainFragment()
        setupCoverClickListener()
    }

    // наблюдаем за созданием группы
    private fun observereateGroupViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedImageUri.collect { uri ->
                    uri?.let {
                        binding.cover.setImageURI(it)
                        binding.cover.background = null
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

    // перехода на MainFragment через btnApply и создание группы
    private fun btnApplyNavigationToMainFragment() {
        binding.btnApply.setOnClickListener {
            val name = (binding.nameCategory.editText as? TextInputEditText)?.text.toString()
            if (name.isNotBlank()) {
                viewModel.createGroup()
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Введите название группы", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // Слушатель для выбора изображения
    private fun setupCoverClickListener() {
        binding.cover.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    // Слушатель для изменения текста в поле имени
    private fun setupNameTextChangeListener() {
        binding.nameCategory.editText?.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                val searchText = text.toString()
                viewModel.setGroupName(searchText)
            }
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}