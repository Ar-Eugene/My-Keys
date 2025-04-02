package com.example.mykeys.newGroup.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mykeys.R
import com.example.mykeys.databinding.ItemGroupBinding
import com.example.mykeys.newGroup.domain.models.GroupModel
import java.util.Collections

class GroupAdapter : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    private var groups: List<GroupModel> = emptyList()
    private var onItemClickListener: ((GroupModel) -> Unit)? = null
    private var onItemMoveListener: ((Int, Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (GroupModel) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnItemMoveListener(listener: (Int, Int) -> Unit) {
        onItemMoveListener = listener
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        val mutableGroups = groups.toMutableList()
        Collections.swap(mutableGroups, fromPosition, toPosition)
        groups = mutableGroups
        notifyItemMoved(fromPosition, toPosition)
    }

    inner class GroupViewHolder(private val binding: ItemGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(groups[position])
                }
            }
        }

        fun bind(group: GroupModel) {
            binding.companyName.text = group.nameGroup

            /// Загрузка изображения с помощью Glide
            if (group.imageGroup == null) {
                binding.companyIcon.setImageResource(R.drawable.placeholder_32px)
            } else {
                Glide.with(binding.root.context)
                    .load(group.imageGroup)
                    .error(R.drawable.placeholder_32px)
                    .into(binding.companyIcon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding = ItemGroupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(groups[position])
    }

    override fun getItemCount(): Int = groups.size

    fun submitList(newList: List<GroupModel>) {
        groups = newList
        notifyDataSetChanged()
    }

}