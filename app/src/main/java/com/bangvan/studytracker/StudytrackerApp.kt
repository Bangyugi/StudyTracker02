package com.bangvan.studytracker

import android.app.Application
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class StudytrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            MobileAds.initialize(
                context = this@StudytrackerApp,
                initializationConfig = InitializationConfig.Builder("ca-app-pub-3940256099942544~3347511713").build()
            ) { initializationStatus ->
            }
        }
    }
}