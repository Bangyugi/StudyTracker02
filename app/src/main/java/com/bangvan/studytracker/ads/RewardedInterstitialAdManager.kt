package com.bangvan.studytracker.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest
import com.google.android.libraries.ads.mobile.sdk.common.FullScreenContentError
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.rewarded.OnUserEarnedRewardListener
import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardItem
import com.google.android.libraries.ads.mobile.sdk.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.libraries.ads.mobile.sdk.rewardedinterstitial.RewardedInterstitialAdEventCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback

class RewardedInterstitialAdManager (private val context: Context) {
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null
    private var isLoading = false
    private val testAdUnitId = "ca-app-pub-3940256099942544/5354046379"
    fun loadAd(adUnitId: String = testAdUnitId) {
        if (isLoading || isAdAvailable()) {
            return
        }
        isLoading = true
        val adRequest = AdRequest.Builder(adUnitId).build()
        RewardedInterstitialAd.load(
            adRequest,
            object : AdLoadCallback<RewardedInterstitialAd> {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    Log.d("RewardedInterstitial", "Tải quảng cáo tặng thưởng thành công!")
                    rewardedInterstitialAd = ad
                    isLoading = false
                }
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("RewardedInterstitial", "Tải quảng cáo thất bại: ${error.message}")
                    rewardedInterstitialAd = null
                    isLoading = false
                }
            }
        )
    }
    fun isAdAvailable(): Boolean {
        return rewardedInterstitialAd != null
    }
    fun showAd(
        activity: Activity,
        onRewardEarned: () -> Unit,
        onAdClosedWithoutReward: () -> Unit,
        onAdFailedToShow: () -> Unit
    ) {
        val ad = rewardedInterstitialAd
        if (ad == null) {
            Log.d("RewardedInterstitial", "Quảng cáo chưa sẵn sàng. Đang tiến hành tải lại...")
            loadAd()
            activity.runOnUiThread {
                onAdFailedToShow()
            }
            return
        }
        var isRewarded = false

        ad.adEventCallback = object : RewardedInterstitialAdEventCallback {
            override fun onAdShowedFullScreenContent() {
                Log.d("RewardedInterstitial", "Đang hiển thị quảng cáo xen kẽ tặng thưởng.")
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d("RewardedInterstitial", "Người dùng đã tắt quảng cáo.")
                rewardedInterstitialAd = null
                loadAd()
                activity.runOnUiThread {
                    if (isRewarded) {
                        onRewardEarned()
                    } else {
                        onAdClosedWithoutReward()
                    }
                }
            }

            override fun onAdFailedToShowFullScreenContent(error: FullScreenContentError) {
                Log.e("RewardedInterstitial", "Hiển thị quảng cáo thất bại: ${error.message}")
                rewardedInterstitialAd = null
                loadAd()
                activity.runOnUiThread {
                    onAdFailedToShow()
                }
            }

            override fun onAdImpression() {
                Log.d("RewardedInterstitial", "Ghi nhận 1 lượt impression.")
            }

            override fun onAdClicked() {
                Log.d("RewardedInterstitial", "Người dùng click vào quảng cáo.")
            }
        }

        ad.show(activity, object : OnUserEarnedRewardListener {
            override fun onUserEarnedReward(rewardItem: RewardItem) {
                Log.d("RewardedInterstitial", "Người dùng đã xem hết quảng cáo và nhận thưởng: ${rewardItem.amount} ${rewardItem.type}")
                isRewarded = true
            }
        })
    }
}
