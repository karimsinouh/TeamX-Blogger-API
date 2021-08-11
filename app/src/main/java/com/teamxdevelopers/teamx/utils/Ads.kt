package com.teamxdevelopers.teamx.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.nativead.NativeAdView
import com.teamxdevelopers.teamx.R

@Composable
fun Banner(
    returnAdView:(AdView)->Unit
){
    AndroidView(
        modifier=Modifier.fillMaxWidth(),
        factory = { context->
            AdView(context).apply {
                adUnitId= context.getString(R.string.banner)
                adSize= AdSize.BANNER
                returnAdView(this)
            }
        }
    )
}