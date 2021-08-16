package com.teamxdevelopers.teamx.ui.viewPost

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebSettingsCompat.setForceDark
import androidx.webkit.WebViewFeature
import com.teamxdevelopers.teamx.R

@Composable
fun ViewPostTopBAr(
    saved:Boolean,
    onSaveChange:(Boolean)->Unit,
    onBackPressed:()->Unit
){
    TopAppBar(
        contentColor= MaterialTheme.colors.onBackground,
        backgroundColor= MaterialTheme.colors.background,
        navigationIcon = { IconButton(onClick = onBackPressed) {
            Icon(Icons.Outlined.ArrowBack,null)
        }
        },
        title = { Text(text = stringResource(id = R.string.app_name))},
        actions = {

            IconButton(onClick = { onSaveChange(!saved) }) {
                if (saved)
                    Icon(painter = painterResource(id = R.drawable.ic_bookmark_filled), contentDescription = null)
                else
                    Icon(painter = painterResource(id = R.drawable.ic_bookmark_outlined), contentDescription = null)

            }

        }
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebComposable(data:String,darkTheme:Boolean){
    return AndroidView(
        factory = {context->
            WebView(context).apply {
                settings.javaScriptEnabled=true

                settings.domStorageEnabled=true
                settings.loadWithOverviewMode=true

                webViewClient=object : WebViewClient(){}
                webChromeClient=object : WebChromeClient(){}

                loadDataWithBaseURL(null,data,"text/html", "UTF-8",null)



                if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK))
                    if(darkTheme)
                        setForceDark(settings, WebSettingsCompat.FORCE_DARK_ON)
                    else
                        setForceDark(settings, WebSettingsCompat.FORCE_DARK_AUTO)

            }
        },
        modifier = Modifier.fillMaxSize()
    )
}


@Preview(showBackground = true)
@Composable
fun PostDetails(){
    Column {

        val painter= painterResource(id = R.drawable.material)

        Image(
            painter =painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier= Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Text(
            text = "Hot to sell those books you got from your parents as a kid",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )
        Text(text = "2 days ago")
    }
}