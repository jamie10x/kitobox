package com.jamie.kitobox

import android.app.Application
import com.jamie.kitobox.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class KitoboxApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@KitoboxApp)
            modules(appModule)
        }
    }
}