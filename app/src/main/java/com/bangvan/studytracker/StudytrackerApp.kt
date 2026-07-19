package com.bangvan.studytracker

import android.app.Application
import com.bangvan.studytracker.ads.AppOpenAdManager
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class StudytrackerApp : Application() {
    lateinit var appOpenAdManager: AppOpenAdManager
        private set
    override fun onCreate() {
        super.onCreate()
        
        appOpenAdManager = AppOpenAdManager(this)

        CoroutineScope(Dispatchers.IO).launch {
            MobileAds.initialize(
                context = this@StudytrackerApp,
                initializationConfig = InitializationConfig.Builder("ca-app-pub-3940256099942544~3347511713")
                    .build()
            ) { initializationStatus ->
                CoroutineScope(Dispatchers.Main).launch {
                    appOpenAdManager.loadAd()
                }
            }
        }
    }
}