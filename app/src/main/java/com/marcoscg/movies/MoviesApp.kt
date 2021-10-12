package com.marcoscg.movies

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.jakewharton.threetenabp.AndroidThreeTen
import com.marcoscg.movies.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoviesApp : Application() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AndroidThreeTen.init(this)

        startKoin {
            androidContext(this@MoviesApp)
            modules(mainModule, popularMoviesModule, upcomingMoviesModule, favoriteMoviesModule, movieDetailsModule)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}