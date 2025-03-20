package com.example.mykeys.newCategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mykeys.databinding.FragmentNewCategoryBinding

class NewCategoryFragment : Fragment() {
    private lateinit var binding: FragmentNewCategoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

}