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
import com.bumptech.glide.load.resource.bitmap.CircleCrop
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
        setupEditButton()
    }

    private fun setupBackButton() {
        binding.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    private fun setupEditButton() {
        binding.btnFab.setOnClickListener {
            args.groupModel?.let { groupModel ->
                findNavController().navigate(
                    DescriptionCategoryFragmentDirections
                        .actionDescriptionCategoryFragmentToNewGroupFragment(
                            groupModel = groupModel,
                            isEditMode = true
                        )
                )
            }
        }
    }
    private fun displayGroupData() {
        val groupModel = args.groupModel

        // передаем Name через args
        binding.nameCategory.text = groupModel?.nameGroup
        binding.nameCategoryContainer.visibility = if (groupModel?.nameGroup.isNullOrBlank()) View.GONE else View.VISIBLE

        // передаем Email через args
        binding.emailCategory.text = groupModel?.emailGroup
        binding.emailCategoryContainer.visibility = if (groupModel?.emailGroup.isNullOrBlank()) View.GONE else View.VISIBLE

        // передаем Password через args
        binding.passwordCategory.text = groupModel?.passwordGroup
        binding.passwordCategoryContainer.visibility = if (groupModel?.passwordGroup.isNullOrBlank()) View.GONE else View.VISIBLE

        // передаем Login через args
        binding.loginCategory.text = groupModel?.loginGroup
        binding.loginCategoryContainer.visibility = if (groupModel?.loginGroup.isNullOrBlank()) View.GONE else View.VISIBLE

        // Загрузка изображения
        loadGroupImage(groupModel?.imageGroup)

    }

    private fun loadGroupImage(imageUri: Uri?) {
        if (imageUri != null) {
            Glide.with(requireContext())
                .load(imageUri)
                .placeholder(R.drawable.cover_placeholder)
                .error(R.drawable.cover_placeholder)
                .transform(CircleCrop())  // Это сделает изображение круглым
                .into(binding.cover)
            // Убираем фоновую рамку, если она есть
            binding.cover.background = null
        } else {
            binding.cover.setImageResource(R.drawable.placeholder_big)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}