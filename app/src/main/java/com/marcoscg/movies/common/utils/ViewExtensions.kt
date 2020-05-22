package com.marcoscg.movies.common.utils

import android.content.res.Resources
import android.view.Gravity
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Snackbar.setAnchorId(anchorId: Int): Snackbar {
    view.apply {
        val layoutParams = view.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.anchorId = anchorId
        layoutParams.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        view.translationY = -8.dp.toFloat() // Preserve bottom margin
        view.layoutParams = layoutParams
    }

    return this
}