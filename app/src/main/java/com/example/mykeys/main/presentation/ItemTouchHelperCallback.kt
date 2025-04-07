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

    override fun isLongPressDragEnabled(): Boolean = false

    override fun isItemViewSwipeEnabled(): Boolean = true

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = 0
        val swipeFlags = ItemTouchHelper.START
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // Поскольку перетаскивание отключено, просто возвращаем false
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        // Вместо прямого удаления, вызываем метод для показа диалога
        adapter.onItemSwiped(position, false) // false означает, что элемент не удаляется сразу
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG || actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            viewHolder?.itemView?.let { view ->
                // Сохраняем оригинальную elevation
                view.tag = ViewCompat.getElevation(view)

                // Увеличиваем elevation для эффекта "поднятия"
                ViewCompat.setElevation(view, 8f)

                // Для перетаскивания можно добавить масштабирование
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    view.scaleX = 1.05f
                    view.scaleY = 1.05f
                }
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.let { view ->
            // Восстанавливаем оригинальную elevation
            val originalElevation = view.tag as? Float ?: 0f
            ViewCompat.setElevation(view, originalElevation)
            // Сбрасываем все трансформации
            view.alpha = 1.0f
            view.translationX = 0f
        }
    }

}