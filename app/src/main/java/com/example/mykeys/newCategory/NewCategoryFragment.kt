package com.example.mykeys.newCategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mykeys.databinding.FragmentNewCategoryBinding
import com.example.mykeys.newCategory.viewmodel.NewCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewCategoryFragment : Fragment() {

    private var _binding: FragmentNewCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewCategoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}