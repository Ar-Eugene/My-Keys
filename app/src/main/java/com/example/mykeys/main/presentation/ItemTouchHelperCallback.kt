package com.example.mykeys.main.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mykeys.R
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
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX < 0) {
            val itemView = viewHolder.itemView
            val cornerRadius = 48f // радиус скругления

            val rectF = RectF(
                itemView.right.toFloat() + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )

            val path = Path()
            val radii = floatArrayOf(
                0f, 0f,            // top-left
                cornerRadius, cornerRadius,  // top-right
                cornerRadius, cornerRadius,  // bottom-right
                0f, 0f             // bottom-left
            )
            path.addRoundRect(rectF, radii, Path.Direction.CW)

            val paint = Paint().apply {
                color = ContextCompat.getColor(context, R.color.delete_group_color)
                isAntiAlias = true
            }

            c.drawPath(path, paint)

            // Рисуем иконку удаления поверх
            val icon = ContextCompat.getDrawable(context, R.drawable.ic_delete)
            icon?.let {
                val iconMargin = (itemView.height - it.intrinsicHeight) / 2
                val iconTop = itemView.top + iconMargin
                val iconBottom = iconTop + it.intrinsicHeight
                val iconLeft = itemView.right - iconMargin - it.intrinsicWidth
                val iconRight = itemView.right - iconMargin

                it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                it.draw(c)
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}