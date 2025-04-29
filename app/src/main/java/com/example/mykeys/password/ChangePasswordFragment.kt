package com.example.mykeys.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

        binding.btnCreatePassword.setOnClickListener {
            val oldPassword = binding.edtOldPassword.text.toString()
            val newPassword = binding.edtChangePassword.text.toString()
            val confirmPassword = binding.edtConfirmChangePassword.text.toString()

            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Пожалуйста, заполните все поля",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!passwordManager.checkPassword(oldPassword)) {
                Toast.makeText(
                    requireContext(),
                    "Вы ввели неверный старый пароль",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(
                    requireContext(),
                    "Новый пароль и подтверждени nне совпадают",
                    Toast.LENGTH_SHORT
                ).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}