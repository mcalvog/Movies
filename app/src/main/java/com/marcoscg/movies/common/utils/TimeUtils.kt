package com.marcoscg.movies.common.utils

import android.content.Context
import com.marcoscg.movies.R

object TimeUtils {

    fun formatMinutes(context: Context, min: Int): String {
        val hours = min / 60
        val minutes = min % 60

        return if (hours <= 1)
            String.format(
                context.resources.getString(R.string.runtime_pattern_single),
                hours.toString(), minutes.toString()
            )
        else
            String.format(
                context.resources.getString(R.string.runtime_pattern_multiple),
                hours.toString(), minutes.toString()
            )
    }

}