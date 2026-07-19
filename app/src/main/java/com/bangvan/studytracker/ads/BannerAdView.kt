package com.bangvan.studytracker.ads

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.libraries.ads.mobile.sdk.banner.AdSize
import com.google.android.libraries.ads.mobile.sdk.banner.AdView
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdEventCallback
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError

@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    adUnitId: String = "ca-app-pub-3940256099942544/9214589741"
) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = {
            context ->
            AdView(context).apply {
                val adSizeForRequest = AdSize.BANNER
                val adRequest = BannerAdRequest.Builder(adUnitId, adSizeForRequest).build()

                loadAd(adRequest, object : AdLoadCallback<BannerAd> {
                    override fun onAdLoaded(bannerAd: BannerAd) {
                        Log.d("AdMob", "Quảng cáo Banner đã được tải thành công!")


                        bannerAd.adEventCallback = object : BannerAdEventCallback {
                            override fun onAdClicked() {
                                Log.d("AdMob", "Người dùng đã nhấp vào quảng cáo.")
                            }
                            override fun onAdImpression() {
                                Log.d("AdMob", "Ghi nhận một lượt hiển thị quảng cáo.")
                            }
                        }
                    }
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.e("AdMob", "Tải quảng cáo thất bại. Mã lỗi: ${error.code}, Thông điệp: ${error.message}")
                    }
                })
            }
        },
        update = { adView ->

        }
    )
}