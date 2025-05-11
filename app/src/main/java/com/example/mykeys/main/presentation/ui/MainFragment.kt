package com.example.mykeys.main.presentation.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainFragmentViewModel by viewModels()
    private lateinit var groupAdapter: GroupAdapter

    private var deleteTimer: CountDownTimer? = null
    private var pendingGroupForDelete: GroupModel? = null
    private var isUndoCancelled = false
    private var snackbar: Snackbar? = null // Выносим snackbar в переменную класса

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
        observeSearchContainerVisibility()
        observePlaceholderVisibility()
        setupClearButton()

        groupAdapter.setOnItemSwipedListener { group, position ->
            val removedGroup = groupAdapter.removeItemTemporarily(position)
            removedGroup?.let {
                showDeleteTrackDialog(it) // запускаем обратный отсчёт и возможность отмены
            }
        }
    }

    // кнопка для перехода на NewGroupFragment
    private fun navigationToNewGroupFragment() {
        binding.btnFab.setOnClickListener {
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

    // отображаем список в recycler
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
            val removedGroup = groupAdapter.removeItemTemporarily(position)
            removedGroup?.let {
                showDeleteTrackDialog(it)
            }

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

    // Наблюдение за видимостью поискового контейнера
    private fun observeSearchContainerVisibility() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isSearchContainerVisible.collect { isVisible ->
                    binding.searchContainer.visibility = if (isVisible) View.VISIBLE else View.GONE
                }
            }
        }
    }

    // Наблюдение за видимостью плейсхолдера
    private fun observePlaceholderVisibility() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isPlaceholderVisible.collect { isVisible ->
                    binding.imPlaceholderMain.visibility = if (isVisible) View.VISIBLE else View.GONE
                }
            }
        }
    }

    // уведомление которое появляется перед удалением элемента
    private fun showDeleteTrackDialog(group: GroupModel) {
        snackbar = Snackbar.make(binding.root, "", Snackbar.LENGTH_INDEFINITE)
        val snackbarLayout = snackbar?.view as? Snackbar.SnackbarLayout
        snackbarLayout?.setPadding(0, 0, 0, 0)

        val customView = LayoutInflater.from(requireContext())
            .inflate(R.layout.snackbar_delete, null)

        val messageText = customView.findViewById<TextView>(R.id.snackbar_text)
        val cancelButton = customView.findViewById<TextView>(R.id.snackbar_cancel)

        var countdown = 5
        isUndoCancelled = false
        pendingGroupForDelete = group

        deleteTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                countdown--
                messageText.text = getString(R.string.delete_chapter) + " (${countdown})"
            }

            override fun onFinish() {
                if (!isUndoCancelled) {
                    viewModel.deleteGroup(group)
                }
                pendingGroupForDelete = null
                snackbar?.dismiss()
            }
        }

        cancelButton.setOnClickListener {
            isUndoCancelled = true
            deleteTimer?.cancel()
            groupAdapter.restoreLastRemovedItem()
            pendingGroupForDelete = null
            snackbar?.dismiss()
        }

        snackbarLayout?.removeAllViews()
        snackbarLayout?.addView(customView)
        snackbar?.show()
        deleteTimer?.start()
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

    override fun onPause() {
        super.onPause()

        // Если экран уходит — отменяем таймер, возвращаем элемент и скрываем Snackbar
        if (pendingGroupForDelete != null && !isUndoCancelled) {
            deleteTimer?.cancel()
            groupAdapter.restoreLastRemovedItem()
            pendingGroupForDelete = null
            snackbar?.dismiss() // Гарантированно скрываем Snackbar
        } else {
            // На всякий случай, если pendingGroupForDelete == null, но Snackbar виден
            snackbar?.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}