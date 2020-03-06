package com.marcoscg.movies.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marcoscg.movies.common.utils.gone
import com.marcoscg.movies.common.utils.visible
import kotlin.math.max

/**
 * Source: https://github.com/chiuki/android-recyclerview
 * Modified by @MarcosCGdev
 */

class AutofitRecyclerView : RecyclerView {

    private var manager: GridLayoutManager? = null
    private var columnWidth = -1
    private var emptyView: View? = null

    private val emptyObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            if (adapter != null && emptyView != null) {
                if (adapter?.itemCount == 0) {
                    emptyView?.visible()
                    this@AutofitRecyclerView.gone()
                } else {
                    emptyView?.gone()
                    this@AutofitRecyclerView.visible()
                }
            }
        }
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)

        adapter?.registerAdapterDataObserver(emptyObserver)
        emptyObserver.onChanged()
    }

    fun setEmptyView(emptyView: View) {
        this.emptyView = emptyView
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attrsArray = intArrayOf(android.R.attr.columnWidth)
            val array = context.obtainStyledAttributes(attrs, attrsArray)
            columnWidth = array.getDimensionPixelSize(0, -1)
            array.recycle()
        }

        manager = GridLayoutManager(getContext(), 1)
        layoutManager = manager
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)

        if (columnWidth > 0) {
            val spanCount = max(1, measuredWidth / columnWidth)
            manager?.spanCount = spanCount
        }
    }
}
