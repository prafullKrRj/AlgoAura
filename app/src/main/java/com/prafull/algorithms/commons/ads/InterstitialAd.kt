package com.prafull.algorithms.commons.ads

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

@Composable
fun InterstitialAdManager(
    adUnitId: String,
    onAdDismissed: () -> Unit
) {
    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }
    var adLoadError by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            adUnitId, // Replace with your AdMob interstitial ad unit ID
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    adLoadError = null
                    Log.d("InterstitialAdManager", "loaded")
                    onAdDismissed()
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                    adLoadError = adError.message
                    Log.d("InterstitialAdManager", "failed to load: ${adError.message}")
                    onAdDismissed()
                }
            }
        )
    }

    fun showInterstitialAdAndNavigate() {
        interstitialAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadInterstitialAd() // Reload the ad for future use
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    loadInterstitialAd() // Reload the ad if it fails to show
                }

                override fun onAdShowedFullScreenContent() {
                    interstitialAd = null // Reset ad after showing it
                }
            }
            ad.show(context as Activity)
        } ?: run {
            onAdDismissed()
        }
    }
    LaunchedEffect(key1 = Unit) {
        Log.d("InterstitialAdManager", "Loading interstitial ad")
        showInterstitialAdAndNavigate()
    }
}