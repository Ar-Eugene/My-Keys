package com.example.mykeys.descriptionCategory

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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
    private var selectedTextView: TextView? = null
    private val checkBoxes by lazy {
        listOf(
            binding.checkBoxName to binding.nameCategory,
            binding.checkBoxEmail to binding.emailCategory,
            binding.checkBoxLogin to binding.loginCategory,
            binding.checkBoxPassword to binding.passwordCategory
        )
    }

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
        setLongPressCopyListeners()
        setupCopySymbolClick()
        setLongPressToShowCheckBoxes()

    }

    private fun setLongPressCopyListeners() {
        val longPressDuration = 1000L // 2 секунды

        val textViews = listOf(
            binding.nameCategory,
            binding.emailCategory,
            binding.loginCategory,
            binding.passwordCategory
        )

        textViews.forEach { textView ->
            textView.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.tag = System.currentTimeMillis()
                        v.postDelayed({
                            if ((System.currentTimeMillis() - (v.tag as Long)) >= longPressDuration) {
                                selectedTextView = v as TextView
                                binding.copySympol.visibility = View.VISIBLE
                            }
                        }, longPressDuration)
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        v.removeCallbacks(null)
                    }
                }
                false
            }
        }
    }
    private fun setLongPressToShowCheckBoxes() {
        val longPressDuration = 500L

        checkBoxes.forEach { (_, textView) ->
            textView.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.tag = System.currentTimeMillis()
                        v.postDelayed({
                            if ((System.currentTimeMillis() - (v.tag as Long)) >= longPressDuration) {
                                showAllCheckBoxes()
                            }
                        }, longPressDuration)
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        v.removeCallbacks(null)
                    }
                }
                false
            }
        }
    }


    private fun showAllCheckBoxes() {
        var atLeastOneVisible = false

        checkBoxes.forEach { (checkBox, textView) ->
            if (!textView.text.isNullOrBlank()) {
                checkBox.visibility = View.VISIBLE
                checkBox.isChecked = false
                atLeastOneVisible = true
            } else {
                checkBox.visibility = View.GONE
            }
        }

        // Показываем иконку копирования только если есть что копировать
        binding.copySympol.visibility = if (atLeastOneVisible) View.VISIBLE else View.GONE
    }

    private fun setupCopySymbolClick() {
        binding.copySympol.setOnClickListener {
            val selectedTexts = checkBoxes
                .filter { it.first.isChecked }
                .map { it.second.text.toString() }
                .filter { it.isNotBlank() }

            if (selectedTexts.isNotEmpty()) {
                val combinedText = selectedTexts.joinToString(separator = "\n")
                val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Copied Text", combinedText)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(requireContext(), "Скопировано:\n$combinedText", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Выберите хотя бы одно поле", Toast.LENGTH_SHORT).show()
            }

            // Скрываем чекбоксы и иконку
            checkBoxes.forEach { it.first.visibility = View.GONE }
            binding.copySympol.visibility = View.GONE
        }
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
        binding.nameCategoryContainer.visibility =
            if (groupModel?.nameGroup.isNullOrBlank()) View.GONE else View.VISIBLE

        // передаем Email через args
        binding.emailCategory.text = groupModel?.emailGroup
        binding.emailCategoryContainer.visibility =
            if (groupModel?.emailGroup.isNullOrBlank()) View.GONE else View.VISIBLE

        // передаем Password через args
        binding.passwordCategory.text = groupModel?.passwordGroup
        binding.passwordCategoryContainer.visibility =
            if (groupModel?.passwordGroup.isNullOrBlank()) View.GONE else View.VISIBLE

        // передаем Login через args
        binding.loginCategory.text = groupModel?.loginGroup
        binding.loginCategoryContainer.visibility =
            if (groupModel?.loginGroup.isNullOrBlank()) View.GONE else View.VISIBLE

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