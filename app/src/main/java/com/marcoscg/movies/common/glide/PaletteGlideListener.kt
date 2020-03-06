package com.marcoscg.movies.common.glide

import android.graphics.Bitmap
import android.view.View
import androidx.palette.graphics.Palette
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class PaletteGlideListener(private val backgroundView: View) :
    RequestListener<Bitmap?> {
    override fun onResourceReady(
        resource: Bitmap?,
        model: Any,
        target: Target<Bitmap?>,
        dataSource: DataSource,
        isFirstResource: Boolean
    ): Boolean {
        resource?.let {
            val palette = Palette.from(resource).generate()

            palette.darkVibrantSwatch?.let {
                backgroundView.setBackgroundColor(it.rgb)
            }
        }
        return false
    }

    override fun onLoadFailed(
        e: GlideException?,
        model: Any,
        target: Target<Bitmap?>,
        isFirstResource: Boolean
    ): Boolean {
        return false
    }

}