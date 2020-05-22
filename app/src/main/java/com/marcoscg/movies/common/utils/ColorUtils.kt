package com.marcoscg.movies.common.utils

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils

object ColorUtils {

    inline val @receiver:ColorInt Int.darken
        @ColorInt
        get() = ColorUtils.blendARGB(this, Color.BLACK, 0.2f)

}