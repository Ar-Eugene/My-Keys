package com.example.mykeys.password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.mykeys.R
import com.example.mykeys.databinding.FragmentEnterPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EnterPasswordFragment : Fragment() {

    private var _binding: FragmentEnterPasswordBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var passwordManager: PasswordManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonEnterPassword.setOnClickListener {
            val password = binding.editTextPassword.text.toString()

            if (password.isEmpty()) {
                Toast.makeText(requireContext(), "Введите пароль", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (passwordManager.checkPassword(password)) {
                // Если пароль верный, переходим на главный экран
                findNavController().navigate(R.id.action_enterPasswordFragment_to_mainFragment)
            } else {
                Toast.makeText(requireContext(), "Неверный пароль", Toast.LENGTH_SHORT).show()
            }
        }

        binding.changePassword.setOnClickListener{
            findNavController().navigate(R.id.action_enterPasswordFragment_to_changePasswordFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}