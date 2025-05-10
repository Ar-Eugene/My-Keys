package com.example.mykeys.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mykeys.R
import com.example.mykeys.databinding.FragmentChangePasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var passwordManager: PasswordManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val getKeyWord = passwordManager.getKeyWord()
        binding.keyword.text = getKeyWord

        // Добавляем слушатель потери фокуса у edtChangePassword
        binding.edtChangePassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePasswordLength()
            }
        }

        // Добавляем слушатель потери фокуса у edtConfirmPassword
        binding.edtChangePassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validConfirmPassword()
            }
        }


        binding.btnCreatePassword.setOnClickListener {
            val oldPassword = binding.edtOldPassword.text.toString()
            val newPassword = binding.edtChangePassword.text.toString()
            val confirmPassword = binding.edtConfirmChangePassword.text.toString()

            if (!validateInputs(oldPassword, newPassword, confirmPassword)) {
                return@setOnClickListener
            }

            passwordManager.savePassword(newPassword)
            Toast.makeText(requireContext(), "Пароль успешно обновлен", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        binding.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    private fun validateInputs(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Boolean {
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Пожалуйста, заполните все поля",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        if (!passwordManager.checkPassword(oldPassword)) {
            Toast.makeText(
                requireContext(),
                "Вы ввели неверный старый пароль",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        // Проверка на совпадение паролей
        if (newPassword != confirmPassword) {
            binding.edtConfirmChangePassword.error =
                getString(R.string.newpassword_confirm_dont_match)
            return false
        } else {
            binding.edtConfirmChangePassword.error = null
        }

        return validatePasswordLength()
    }

    // валидация на длину
    private fun validatePasswordLength(): Boolean {
        val newPassword = binding.edtChangePassword.text.toString()
        var isValid = newPassword.length >= 5

        if (newPassword.isNotEmpty() && !isValid) {
            binding.edtChangePassword.error = getString(R.string.error_password_too_short)
            return false
        } else {
            binding.edtChangePassword.error = null
        }
        return isValid
    }

    // валидация на равенство confirmPassword с newPassword
    private fun validConfirmPassword(): Boolean {
        val newPassword = binding.edtChangePassword.text.toString()
        val confirmPassword = binding.edtConfirmChangePassword.text.toString()
        var isValid = confirmPassword == newPassword
        if (confirmPassword != newPassword) {
            binding.edtConfirmChangePassword.error =
                getString(R.string.newpassword_confirm_dont_match)
            return false
        } else {
            binding.edtConfirmChangePassword.error = null
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}