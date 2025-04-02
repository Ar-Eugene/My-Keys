package com.example.mykeys.main.presentation

import android.content.Context
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mykeys.newGroup.presentation.GroupAdapter

class ItemTouchHelperCallback(
    private val adapter: GroupAdapter,
    private val context: Context
) : ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean = true

    override fun isItemViewSwipeEnabled(): Boolean = false

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition
        adapter.onItemMove(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Не используется, так как свайп отключен
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder?.itemView?.let { view ->
                // Добавляем визуальные эффекты при перетаскивании
                ViewCompat.setElevation(view, 8f)
                view.alpha = 0.9f
                view.scaleX = 1.05f
                view.scaleY = 1.05f
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        // Возвращаем вид в нормальное состояние
        viewHolder.itemView.let { view ->
            ViewCompat.setElevation(view, 0f)
            view.alpha = 1.0f
            view.scaleX = 1.0f
            view.scaleY = 1.0f
        }
    }
}