package com.example.mykeys.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mykeys.databinding.FragmentGroupsBinding


class GroupsFragment : Fragment() {
    private lateinit var binding: FragmentGroupsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

}