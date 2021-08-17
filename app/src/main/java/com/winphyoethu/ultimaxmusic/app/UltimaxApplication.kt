package com.winphyoethu.ultimaxmusic.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class UltimaxApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }

}