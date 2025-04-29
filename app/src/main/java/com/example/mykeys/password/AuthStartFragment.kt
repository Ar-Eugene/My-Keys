package com.example.mykeys.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mykeys.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthStartFragment : Fragment() {

    @Inject
    lateinit var passwordManager: PasswordManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Этот фрагмент не имеет UI, он только определяет, куда перейти дальше
        return inflater.inflate(R.layout.fragment_auth_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Проверяем, установлен ли пароль
        if (passwordManager.isPasswordSet()) {
            // Если пароль уже установлен, переходим на экран ввода пароля
            findNavController().navigate(R.id.action_authStartFragment_to_enterPasswordFragment)
        } else {
            // Если пароль еще не установлен, переходим на экран создания пароля
            findNavController().navigate(R.id.action_authStartFragment_to_createPasswordFragment)
        }
    }
}