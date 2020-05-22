package com.marcoscg.movies.base

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.marcoscg.movies.common.utils.WindowUtils.clearStatusBar
import com.marcoscg.movies.common.utils.WindowUtils.setToolbarTopPadding
import com.marcoscg.movies.common.utils.WindowUtils.setTransparentStatusBar
import com.marcoscg.movies.common.utils.WindowUtils.setupDarkTheme
import com.marcoscg.movies.common.utils.WindowUtils.setupLightTheme

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTransparentStatusBar(this)
        applyTheme()
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)

        toolbar?.let {
            setToolbarTopPadding(toolbar)
        }
    }

    fun clearStatusBar() {
        clearStatusBar(this)
    }

    private fun applyTheme() {
        when(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                setupDarkTheme(this)
            }
            else -> {
                setupLightTheme(this)
            }
        }
    }
}