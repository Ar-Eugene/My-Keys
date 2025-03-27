package com.example.mykeys.newGroup.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mykeys.R
import com.example.mykeys.databinding.FragmentNewGroupBinding
import com.example.mykeys.newGroup.presentation.viewmodel.NewGroupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewGroupFragment : Fragment() {

    private var _binding: FragmentNewGroupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewGroupViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        clickListener()

    }

    // хранятся все clickListener
    private fun clickListener() {
        navigationToNewGroupFragment()
    }

    // наблюдаем за всеми изменениями VM
    private fun observeViewModel() {
        observeNavigationEvent()
    }

    // кнопки для перехода на MainFragment
    private fun navigationToNewGroupFragment() {
        binding.btnApply.setOnClickListener {
            viewModel.onApplyButtonClick()
        }
        binding.backArrow.setOnClickListener {
            viewModel.onApplyButtonClick()
        }
    }

    // Наблюдение за событиями навигации
    private fun observeNavigationEvent() {
        viewModel.navigationEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is NewGroupViewModel.NavigationEvent.NavigationToMainFragment -> {
                    findNavController().navigate(R.id.action_newGroupFragment_to_mainFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}