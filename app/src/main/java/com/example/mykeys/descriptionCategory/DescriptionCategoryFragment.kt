package com.example.mykeys.descriptionCategory

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mykeys.R
import com.example.mykeys.databinding.FragmentDescriptionCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DescriptionCategoryFragment : Fragment() {

    private var _binding: FragmentDescriptionCategoryBinding? = null
    private val binding get() = _binding!!

    private val args: DescriptionCategoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescriptionCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackButton()
        displayGroupData()
    }

    private fun setupBackButton() {
        binding.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun displayGroupData() {
        val groupModel = args.groupModel

        // Подставляем текст
        binding.nameCategory.text = groupModel?.nameGroup

        // Загрузка изображения
        loadGroupImage(groupModel?.imageGroup)

    }

    private fun loadGroupImage(imageUri: Uri?) {
        if (imageUri != null) {
            Glide.with(requireContext())
                .load(imageUri)
                .placeholder(R.drawable.cover_placeholder)
                .error(R.drawable.cover_placeholder)
                .into(binding.cover)
            // Убираем фоновую рамку, если она есть
            binding.cover.background = null
        } else {
            binding.cover.setImageResource(R.drawable.cover_placeholder)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}