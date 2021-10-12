package com.marcoscg.movies.common.glide

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.marcoscg.movies.R
import com.marcoscg.movies.common.utils.ColorUtils

fun ImageView.load(url: String, crossFade: Boolean = false, width: Int = -1,
                   height: Int = -1, onLoadingFinished: (color: Int) -> Unit = {}) {

    val listener = object : RequestListener<Bitmap> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Bitmap>?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished(Color.DKGRAY)

            return false
        }

        override fun onResourceReady(
            resource: Bitmap?,
            model: Any?,
            target: Target<Bitmap>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            if (resource != null) {
                val palette = Palette.from(resource).generate()
                var color = palette.getVibrantColor(palette.getMutedColor(Color.DKGRAY))

                if (ColorUtils.isDark(color).not()) {
                    color = palette.getDarkVibrantColor(Color.DKGRAY)
                }

                onLoadingFinished(color)
            } else {
                onLoadingFinished(Color.DKGRAY)
            }

            return false
        }
    }

    val glide = Glide.with(this).asBitmap()
        .load(url)
        .apply(RequestOptions.placeholderOf(R.drawable.poster_placeholder))
        .centerCrop()
        .listener(listener)

    if (crossFade)
        glide.transition(BitmapTransitionOptions.withCrossFade())

    if (width != -1 && height != -1)
        glide.override(width, height)

    glide.into(this)
}