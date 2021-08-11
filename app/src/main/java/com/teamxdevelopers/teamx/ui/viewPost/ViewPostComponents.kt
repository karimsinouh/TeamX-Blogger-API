package com.teamxdevelopers.teamx.ui.viewPost

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
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
        title = {},
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
fun WebComposable(data:String){
    return AndroidView(
        factory = {context->
            WebView(context).apply {
                settings.javaScriptEnabled=true

                settings.domStorageEnabled=true
                settings.loadWithOverviewMode=true

                webViewClient=object : WebViewClient(){}
                webChromeClient=object : WebChromeClient(){}


                loadDataWithBaseURL(null,data,"text/html", "UTF-8",null)

            }
        },
        modifier = Modifier.fillMaxSize()
    )
}