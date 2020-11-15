package com.marcoscg.movies.ui.splash

import android.os.Bundle
import com.marcoscg.movies.base.BaseActivity
import com.marcoscg.movies.common.navigator.navigateToHome

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigateToHome()
    }

}