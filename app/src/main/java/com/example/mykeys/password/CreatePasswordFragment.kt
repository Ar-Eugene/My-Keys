package com.example.mykeys.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mykeys.R
import com.example.mykeys.databinding.FragmentCreatePasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreatePasswordFragment : Fragment() {

    private var _binding: FragmentCreatePasswordBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var passwordManager: PasswordManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Добавляем слушатель потери фокуса у edtPassword
        binding.edtPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePasswordLength()
            }
        }

        // Добавляем слушатель потери фокуса у edtConfirmPassword
        binding.edtConfirmPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validConfirmPassword()
            }
        }

        binding.btnCreatePassword.setOnClickListener {
            val password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtConfirmPassword.text.toString()
            val keyWord = binding.edtConfirmKeyword.text.toString()

            if (!validateInputs(password, confirmPassword, keyWord)) {
                return@setOnClickListener
            }

            passwordManager.savePassword(password)
            passwordManager.saveKeyWord(keyWord)

            // Навигация
            findNavController().navigate(R.id.action_createPasswordFragment_to_enterPasswordFragment)
        }

    }

    private fun validateInputs(
        password: String,
        confirmPassword: String,
        keyWord: String
    ): Boolean {
        if (password.isEmpty() || confirmPassword.isEmpty() || keyWord.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Пожалуйста, заполните все поля",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        // Проверка на совпадение паролей
        if (password != confirmPassword) {
            binding.edtConfirmPassword.error = getString(R.string.password_not_confirm)
            return false
        } else {
            binding.edtConfirmPassword.error = null
        }

        // Проверка ключевого слова
        if (keyWord.isEmpty()) {
            binding.edtConfirmKeyword.error = "Укажите ключевое слово"
        } else {
            binding.edtConfirmKeyword.error = null
        }

        return validatePasswordLength()
    }

    // валидация на длину
    private fun validatePasswordLength(): Boolean {
        val newPassword = binding.edtPassword.text.toString()
        var isValid = newPassword.length <= 5

        if (newPassword.isNotEmpty() && !isValid) {
            binding.edtPassword.error = getString(R.string.error_password_too_short)
            return false
        } else {
            binding.edtPassword.error = null
        }
        return isValid
    }

    // валидация на равенство confirmPassword с password
    private fun validConfirmPassword(): Boolean {
        val password = binding.edtPassword.text.toString()
        val confirmPassword = binding.edtConfirmPassword.text.toString()
        var isValid = confirmPassword == password
        if (confirmPassword != password) {
            binding.edtConfirmPassword.error = getString(R.string.password_not_confirm)
            return false
        } else {
            binding.edtConfirmPassword.error = null
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}