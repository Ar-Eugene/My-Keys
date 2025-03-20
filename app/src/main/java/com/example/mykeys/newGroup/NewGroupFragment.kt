package com.example.mykeys.newGroup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mykeys.databinding.FragmentNewGroupBinding

class NewGroupFragment : Fragment() {
    private lateinit var binding: FragmentNewGroupBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

}