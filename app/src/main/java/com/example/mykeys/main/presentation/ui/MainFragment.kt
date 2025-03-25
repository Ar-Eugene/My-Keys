package com.example.mykeys.main.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mykeys.R
import com.example.mykeys.databinding.FragmentMainBinding
import com.example.mykeys.main.presentation.viewmodel.MainFragmentViewModel


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainFragmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        clickListener()
        setupSearchTextWatcher()

    }

    // хранятся все clickListener
    private fun clickListener() {
        navigationToNewGroupFragment()
    }

    // наблюдаем за всеми изменениями VM
    private fun observeViewModel() {
        observeNavigationEvent()
        observeSearchTextWatcher()
    }

    // кнопка для переходан на NewGroupFragment
    private fun navigationToNewGroupFragment() {
        binding.btnApply.setOnClickListener {
            viewModel.onApplyButtonClick()
        }
    }

    // Наблюдение за событиями навигации
    private fun observeNavigationEvent() {
        viewModel.navigationEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is MainFragmentViewModel.NavigationEvent.NavigationToNewGroupFragment -> {
                    findNavController().navigate(R.id.action_mainFragment_to_newGroupFragment)
                }
            }
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

    // Наблюдение за изменением текста
    private fun observeSearchTextWatcher() {
        viewModel.searchText.observe(viewLifecycleOwner) { text ->
            if (binding.edittextSearch.text.toString() != text) {
                binding.edittextSearch.setText(text)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}