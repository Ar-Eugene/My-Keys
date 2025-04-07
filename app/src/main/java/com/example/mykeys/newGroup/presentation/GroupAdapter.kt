package com.example.mykeys.newGroup.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mykeys.R
import com.example.mykeys.databinding.ItemGroupBinding
import com.example.mykeys.newGroup.domain.models.GroupModel

class GroupAdapter : RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {
    private var groups: List<GroupModel> = emptyList()
    private var onItemClickListener: ((GroupModel) -> Unit)? = null
    private var onItemSwipedListener: ((GroupModel, Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (GroupModel) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnItemSwipedListener(listener: (GroupModel, Int) -> Unit) {
        onItemSwipedListener = listener
    }

    fun onItemSwiped(position: Int, isDeleted: Boolean) {
        if (position != RecyclerView.NO_POSITION && position < groups.size) {
            val group = groups[position]
            if (isDeleted) {
                // Если элемент действительно удален, обновляем список
                val newList = groups.toMutableList()
                newList.removeAt(position)
                groups = newList
                notifyItemRemoved(position)
            } else {
                // Если элемент не удален, возвращаем его на место
                notifyItemChanged(position)
                // Вызываем слушателя для показа диалога
                onItemSwipedListener?.invoke(group, position)
            }
        }
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