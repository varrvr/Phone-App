package com.example.phoneapp.ui.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneapp.ui.adapter.SectionedExpandableAdapter

class SectionSpacingDecoration(
    private val headerBottom: Int,
    private val itemBottom: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view).coerceAtLeast(0)
        val adapter = parent.adapter ?: return

        if (adapter.getItemViewType(position) == SectionedExpandableAdapter.TYPE_SECTION) {
            outRect.set(0, 0, 0, headerBottom)
        } else {
            outRect.set(0, 0, 0, itemBottom)
        }
    }
}