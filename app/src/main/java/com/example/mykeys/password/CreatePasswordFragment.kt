package com.example.mykeys.password

import android.os.Bundle
import android.util.Log
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

        binding.btnCreatePassword.setOnClickListener {
            val password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtConfirmPassword.text.toString()
            val keyWord = binding.edtConfirmKeyword.text.toString()

            var isValid = true

            // Проверка длины пароля
            if (password.isEmpty() || password.length < 5) {
                binding.edtPassword.error = getString(R.string.error_password_too_short)
                isValid = false
            } else {
                binding.edtPassword.error = null
            }

            // Проверка повторного пароля
            if (confirmPassword.isEmpty()) {
                binding.edtConfirmPassword.error = "Повторите пароль"
                isValid = false
            } else if (password != confirmPassword) {
                binding.edtConfirmPassword.error = "Пароли не совпадают"
                isValid = false
            } else {
                binding.edtConfirmPassword.error = null
            }

            // Проверка ключевого слова
            if (keyWord.isEmpty()) {
                binding.edtConfirmKeyword.error = "Укажите ключевое слово"
                isValid = false
            } else {
                binding.edtConfirmKeyword.error = null
            }

            if (!isValid) return@setOnClickListener

            // Сохраняем данные
            passwordManager.savePassword(password)
            passwordManager.saveKeyWord(keyWord)
            Log.d("PasswordManager", "Saved keyword: $keyWord")

            // Навигация
            findNavController().navigate(R.id.action_createPasswordFragment_to_enterPasswordFragment)
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}