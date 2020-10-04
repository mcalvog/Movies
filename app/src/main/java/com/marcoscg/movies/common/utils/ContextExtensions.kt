package com.marcoscg.movies.common.utils

import android.content.Context
import androidx.annotation.DimenRes

fun Context.getDimen(@DimenRes dimen: Int): Int {
    return this.resources.getDimensionPixelSize(dimen)
}