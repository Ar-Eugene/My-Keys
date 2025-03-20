package com.example.mykeys.descriptionCategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mykeys.databinding.FragmentDescriptionCategoryBinding

class DescriptionCategoryFragment : Fragment() {
    private lateinit var binding: FragmentDescriptionCategoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDescriptionCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }
}