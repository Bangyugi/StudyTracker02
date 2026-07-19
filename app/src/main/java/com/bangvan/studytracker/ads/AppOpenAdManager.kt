package com.bangvan.studytracker.ads

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAd
import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAdEventCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

/** Listener to be notified when an app open ad is complete. */
interface OnShowAdCompleteListener {
    fun onShowAdComplete()
}

class AppOpenAdManager(private val myApplication: Application) : 
    Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    private var isShowingAd = false
    private var currentActivity: Activity? = null
    private var loadTime: Long = 0

    // Biến lưu trữ listener khi splash screen đang đợi ad tải về
    private var showAdCompleteListener: OnShowAdCompleteListener? = null

    private val adUnitId = "ca-app-pub-3940256099942544/9257395921"

    init {
        myApplication.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun loadAd() {
        if (isLoadingAd || isAdAvailable()) {
            return
        }
        isLoadingAd = true
        val request = AdRequest.Builder(adUnitId).build()

        AppOpenAd.load(
            request,
            object : AdLoadCallback<AppOpenAd> {
                override fun onAdLoaded(ad: AppOpenAd) {
                    Log.d("AppOpenAd", "Tải quảng cáo App Open thành công!")
                    appOpenAd = ad
                    isLoadingAd = false
                    loadTime = Date().time

                    // Nếu có splash screen đang đợi tải ad, hiển thị ngay lập tức
                    val listener = showAdCompleteListener
                    val activity = currentActivity
                    if (listener != null && activity != null) {
                        showAdCompleteListener = null
                        showAdIfAvailable(activity, listener)
                    }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e("AppOpenAd", "Tải quảng cáo thất bại: ${adError.message}")
                    isLoadingAd = false

                    // Nếu có splash screen đang đợi, báo hoàn thành để bỏ qua ad và vào app
                    showAdCompleteListener?.onShowAdComplete()
                    showAdCompleteListener = null
                }
            }
        )
    }

    private fun isAdAvailable(): Boolean {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
    }

    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numHours * numMilliSecondsPerHour
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        val activity = currentActivity
        if (activity != null) {
            showAdIfAvailable(activity, null)
        }
    }

    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener?) {
        if (isShowingAd) {
            Log.d("AppOpenAd", "App open ad is already showing.")
            onShowAdCompleteListener?.onShowAdComplete()
            return
        }

        if (!isAdAvailable()) {
            Log.d("AppOpenAd", "App open ad is not ready yet. Đang chờ tải...")
            
            if (onShowAdCompleteListener != null) {
                showAdCompleteListener = onShowAdCompleteListener

                CoroutineScope(Dispatchers.Main).launch {
                    delay(4000)
                    if (showAdCompleteListener != null) {
                        Log.d("AppOpenAd", "Chờ tải quảng cáo quá 4 giây (Timeout). Bỏ qua quảng cáo và vào app.")
                        showAdCompleteListener?.onShowAdComplete()
                        showAdCompleteListener = null
                    }
                }
                
                // Kích hoạt tiến trình tải ad (nếu chưa chạy)
                loadAd()
            }
            return
        }

        appOpenAd?.adEventCallback = object : AppOpenAdEventCallback {
            override fun onAdShowedFullScreenContent() {
                Log.d("AppOpenAd", "App open ad showed.")
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d("AppOpenAd", "App open ad dismissed.")
                appOpenAd = null
                isShowingAd = false
                onShowAdCompleteListener?.onShowAdComplete()
                loadAd()
            }

            override fun onAdFailedToShowFullScreenContent(error: FullScreenContentError) {
                Log.w("AppOpenAd", "App open ad failed to show: ${error.message}")
                appOpenAd = null
                isShowingAd = false
                onShowAdCompleteListener?.onShowAdComplete()
                loadAd()
            }

            override fun onAdImpression() {
                Log.d("AppOpenAd", "App open ad recorded an impression.")
            }

            override fun onAdClicked() {
                Log.d("AppOpenAd", "App open ad recorded a click.")
            }
        }

        isShowingAd = true
        appOpenAd?.show(activity)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }
    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {
        if (currentActivity == activity) {
            currentActivity = null
        }
    }
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        if (currentActivity == activity) {
            currentActivity = null
        }
    }
}