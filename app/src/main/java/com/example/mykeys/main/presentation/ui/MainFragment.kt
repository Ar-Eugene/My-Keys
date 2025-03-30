package com.example.mykeys.main.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mykeys.R
import com.example.mykeys.databinding.FragmentMainBinding
import com.example.mykeys.main.presentation.viewmodel.MainFragmentViewModel
import com.example.mykeys.newGroup.presentation.GroupAdapter
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
        }
        groupAdapter.setOnItemClickListener { group ->
            Toast.makeText(
                requireContext(),
                "Выбрана группа: ${group.nameGroup}",
                Toast.LENGTH_SHORT
            ).show()
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
                viewModel.groups.collect { groups ->
                    groupAdapter.submitList(groups)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}