package com.marcoscg.movies.common.widget

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class CustomSwipeRefreshLayout : SwipeRefreshLayout {
    constructor(context: Context) : super(context)
    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs)

    init {
        setProgressViewOffset (true, progressViewStartOffset, progressViewEndOffset)
    }
}