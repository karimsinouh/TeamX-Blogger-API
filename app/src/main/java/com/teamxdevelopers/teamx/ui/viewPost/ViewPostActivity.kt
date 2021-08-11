package com.teamxdevelopers.teamx.ui.viewPost

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.teamxdevelopers.teamx.R
import com.teamxdevelopers.teamx.database.saved.SavedParcelable
import com.teamxdevelopers.teamx.ui.theme.BloggerAPITheme
import com.teamxdevelopers.teamx.utils.Banner
import com.teamxdevelopers.teamx.utils.CenterProgress
import com.teamxdevelopers.teamx.utils.DarkTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ViewPostActivity: ComponentActivity() {

    private lateinit var savedPost:SavedParcelable
    private var postId:String?=null
    private val vm by viewModels<ViewPostViewModel>()
    private lateinit var adRequest:AdRequest

    @Inject lateinit var darkTheme: DarkTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_BloggerAPI)

        intent.getStringExtra("postId")?.let { _postId->
            postId=_postId
            if (vm.post.value==null)
                lifecycleScope.launch {
                    vm.loadPost(_postId)
                }
        }

        intent.getParcelableExtra<SavedParcelable>("savedPost")?.let {
            savedPost=it
        }

        adRequest=AdRequest.Builder().build()

        setContent {

            BloggerAPITheme(darkTheme.isEnabled()) {
                window.statusBarColor= MaterialTheme.colors.background.toArgb()

                Column {
                    vm.post.value.let {post->
                        if (post==null && postId!=null)
                            CenterProgress()
                        else if (postId==null)
                            WebComposable(data = savedPost.content)
                        else{

                            val saved=vm.exists(post?.id!!).observeAsState(false)

                            ViewPostTopBAr(
                                saved=saved.value,
                                onBackPressed = { finish() },
                                onSaveChange = {
                                    vm.onSave(it)
                                }
                            )

                            Banner{
                                it.loadAd(adRequest)
                            }

                            WebComposable(data = post.content)
                        }
                    }

                }
            }

        }
    }

}

