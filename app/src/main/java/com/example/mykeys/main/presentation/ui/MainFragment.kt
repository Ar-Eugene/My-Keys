package com.example.mykeys.main.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    }

    // хранятся все clickListener
    private fun clickListener() {
        navigationToNewGroupFragment()
    }

    // кнопка для переходан на NewGroupFragment
    private fun navigationToNewGroupFragment() {
        binding.btnApply.setOnClickListener {
            viewModel.onApplyButtonClick()
        }
    }

    // наблюдаем за всеми изменениями VM
    private fun observeViewModel() {
        observeNavigationEvent()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}