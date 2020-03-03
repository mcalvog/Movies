package com.marcoscg.movies.ui.splash

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.marcoscg.movies.R
import com.marcoscg.movies.common.navigator.navigateToHome

class SplashActivity : AppCompatActivity() {

    private var finish = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            if (finish) {
                navigateToHome()
            }
        }, SPLASH_TIME_OUT)
    }

    override fun onPause() {
        super.onPause()

        finish = false
    }

    override fun onResume() {
        super.onResume()

        if (finish.not()) {
            navigateToHome()
        }
    }

    companion object {
        private const val SPLASH_TIME_OUT = 3000L
    }
}