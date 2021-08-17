package com.teamxdevelopers.teamx.ui.viewPost

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
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
import com.google.accompanist.coil.rememberCoilPainter
import com.teamxdevelopers.teamx.R
import com.teamxdevelopers.teamx.data.Post
import com.teamxdevelopers.teamx.utils.ChipsList

@Composable
fun ViewPostTopBAr(
    saved:Boolean,
    onSaveChange:(Boolean)->Unit,
    onBackPressed:()->Unit,
    onShare:()->Unit
){
    TopAppBar(
        contentColor= MaterialTheme.colors.onBackground,
        backgroundColor= MaterialTheme.colors.background,
        navigationIcon = { IconButton(onClick = onBackPressed) {
            Icon(Icons.Outlined.ArrowBack,null)
        } },
        title = { Text(text = stringResource(id = R.string.app_name))},
        actions = {

            IconButton(onClick = { onSaveChange(!saved) }) {
                if (saved)
                    Icon(painter = painterResource(id = R.drawable.ic_bookmark_filled), contentDescription = null)
                else
                    Icon(painter = painterResource(id = R.drawable.ic_bookmark_outlined), contentDescription = null)

            }

            IconButton(onClick=onShare){
                Icon(Icons.Outlined.Share , contentDescription = null)
            }

        },
        elevation = 0.dp,
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebComposable(data:String,darkTheme:Boolean,returnWebView:(WebView)->Unit){

    val color=MaterialTheme.colors.background.toArgb()

    return AndroidView(
        factory = {context->
            WebView(context).apply {

                setBackgroundColor(color)
                settings.javaScriptEnabled=true

                settings.domStorageEnabled=true
                settings.loadWithOverviewMode=true

                webViewClient=object : WebViewClient(){
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)

                        if (
                            url!=null
                            && !url.contains("googleusercontent")
                            && url!="about:blank"
                        ){
                            val intent=Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    }

                }

                val editedData=if(darkTheme)
                    data.replace("white","#121212")
                        .replace("#222222","#ffffff")
                        .replace("#333333","#ffffff")
                        .replace("#111111","#ffffff")
                        .replace("#44444","#ffffff")
                        .replace("#000000","#ffffff")
                else
                    data

                loadDataWithBaseURL(null,editedData,"text/html", "UTF-8",null)

                returnWebView(this)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    )
}



@Composable
fun PostDetails(post:Post){
    Column {



        Column(modifier = Modifier.padding(12.dp),verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = post.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
            Text(text = post.published)

            ChipsList(
                chips = post.labels(),
                colored=false,
                onChipClick = {}
            )
        }

    }
}