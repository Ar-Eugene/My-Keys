package com.example.mykeys.main.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mykeys.R
import com.example.mykeys.databinding.FragmentMainBinding
import com.example.mykeys.main.presentation.ItemTouchHelperCallback
import com.example.mykeys.main.presentation.viewmodel.MainFragmentViewModel
import com.example.mykeys.newGroup.domain.models.GroupModel
import com.example.mykeys.newGroup.presentation.GroupAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainFragmentViewModel by viewModels()
    private lateinit var groupAdapter: GroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchTextWatcher()
        navigationToNewGroupFragment()
        setupRecyclerView()
        observeSearchTextWatcher()
        observeGroups()
        setupClearButton()
    }

    // кнопка для перехода на NewGroupFragment
    private fun navigationToNewGroupFragment() {
        binding.btnApply.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_newGroupFragment)
        }
    }

    // настройка отслеживания изменений текста
    private fun setupSearchTextWatcher() {
        binding.edittextSearch.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                updateClearButtonIcon(text)
                val searchText = text.toString()
                viewModel.updateSearchText(searchText)
            }
        )
    }

    // отображаем наш список в recycler
    private fun setupRecyclerView() {
        groupAdapter = GroupAdapter()
        binding.recycler.apply {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(requireContext())

            // Настройка ItemTouchHelper для swape
            val callback = ItemTouchHelperCallback(groupAdapter, requireContext())
            val itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(this)
        }

        // Обработчик клика по элементу
        groupAdapter.setOnItemClickListener { group ->
            val action =
                MainFragmentDirections.actionMainFragmentToDescriptionCategoryFragment(group)
            findNavController().navigate(action)
        }

        // Обработчик перемещения элементов по горизонтали + удаление
        groupAdapter.setOnItemSwipedListener { group, position ->
            showDeleteTrackDialog(group)
        }
    }

    // Наблюдение за изменением текста
    private fun observeSearchTextWatcher() {
        viewModel.searchText.observe(viewLifecycleOwner) { text ->
            if (binding.edittextSearch.text.toString() != text) {
                binding.edittextSearch.setText(text)
            }
        }
    }

    // Наблюдение за созданием новой группы
    private fun observeGroups() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filterGroups.observe(viewLifecycleOwner) { groups ->
                    groupAdapter.submitList(groups)
                }
            }
        }
    }

    // уведомление которое появляется перед удалением элемента
    private fun showDeleteTrackDialog(group: GroupModel) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_chapter))
            .setMessage(getString(R.string.delete_charter_message))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                // Отменяем удаление и возвращаем элемент на место
                groupAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.delete)) { dialog, _ ->
                // Удаляем элемент
                viewModel.deleteGroup(group)
                dialog.dismiss()
            }
            .show()
    }

    // обновление иконки кнопки
    private fun updateClearButtonIcon(text: CharSequence?) {
        binding.buttonClear.setImageResource(
            if (text?.isNotEmpty() == true) {
                R.drawable.edit_text_clear_button
            } else {
                R.drawable.ic_search
            }
        )
    }

    // настройка кнопки очистки поиска
    private fun setupClearButton() {
        binding.buttonClear.setOnClickListener {
            // Если поле поиска не пустое, очищаем его
            if (binding.edittextSearch.text.isNotEmpty()) {
                binding.edittextSearch.setText("")
                viewModel.updateSearchText("")
            }
        }
        // Инициализация иконки кнопки
        updateClearButtonIcon(binding.edittextSearch.text)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}