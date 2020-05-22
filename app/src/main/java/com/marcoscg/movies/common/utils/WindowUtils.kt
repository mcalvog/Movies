package com.marcoscg.movies.common.utils

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import com.marcoscg.movies.R

object WindowUtils {

    fun setupLightTheme(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            setLightStatusBar(activity)
        }

        if (Build.VERSION.SDK_INT >= 26) {
            setLightNavigationBar(activity)
        }
    }

    /**
     * Some devices need to clear light status and navigation bar by default
     */
    fun setupDarkTheme(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            setDarkStatusBar(activity)
        }

        if (Build.VERSION.SDK_INT >= 26) {
            setDarkNavigationBar(activity)
        }
    }

    fun setTransparentStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    fun setToolbarTopPadding(toolbar: Toolbar) {
        toolbar.setOnApplyWindowInsetsListener { v, insets ->
            v.updatePadding(top = insets.systemWindowInsetTop)
            insets
        }
    }

    fun clearStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            setDarkStatusBar(activity, false)
        }

        var flags = activity.window.decorView.systemUiVisibility
        flags = flags and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.inv()
        activity.window.decorView.systemUiVisibility = flags
    }

    @TargetApi(23)
    private fun setLightStatusBar(activity: Activity, colored: Boolean = true) {
        var flags = activity.window.decorView.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        activity.window.decorView.systemUiVisibility = flags

        if (colored)
            activity.window.statusBarColor = ContextCompat.getColor(activity, R.color.colorStatusBarLight)
    }

    @TargetApi(23)
    private fun setDarkStatusBar(activity: Activity, colored: Boolean = true) {
        var flags = activity.window.decorView.systemUiVisibility
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        activity.window.decorView.systemUiVisibility = flags

        if (colored)
            activity.window.statusBarColor = ContextCompat.getColor(activity, R.color.colorStatusBarDark)
    }

    @TargetApi(26)
    private fun setLightNavigationBar(activity: Activity) {
        var flags = activity.window.decorView.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        activity.window.decorView.systemUiVisibility = flags
        activity.window.navigationBarColor = Color.WHITE
    }

    @TargetApi(26)
    private fun setDarkNavigationBar(activity: Activity) {
        var flags = activity.window.decorView.systemUiVisibility
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        activity.window.decorView.systemUiVisibility = flags
        activity.window.navigationBarColor = ContextCompat.getColor(activity, R.color.colorBackground)
    }

}