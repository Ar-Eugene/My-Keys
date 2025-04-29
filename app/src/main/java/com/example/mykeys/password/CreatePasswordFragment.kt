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

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Пароль не может быть пустым", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (keyWord.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Должно быть указано ключевое слово",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Сохраняем пароль
            passwordManager.savePassword(password)

            // Сохраняем пароль
            passwordManager.saveKeyWord(keyWord)
            Log.d("PasswordManager", "Saved keyword: $keyWord")

            // Переходим на экран ввода пароля
            findNavController().navigate(R.id.action_createPasswordFragment_to_enterPasswordFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}