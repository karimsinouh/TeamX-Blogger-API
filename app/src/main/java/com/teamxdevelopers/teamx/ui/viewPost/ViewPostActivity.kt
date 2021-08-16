package com.teamxdevelopers.teamx.ui.viewPost

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.lifecycleScope
import androidx.webkit.WebSettingsCompat
import com.google.android.gms.ads.AdRequest
import com.teamxdevelopers.teamx.R
import com.teamxdevelopers.teamx.data.Post
import com.teamxdevelopers.teamx.database.saved.SavedParcelable
import com.teamxdevelopers.teamx.ui.main.StickyHeader
import com.teamxdevelopers.teamx.ui.theme.BloggerAPITheme
import com.teamxdevelopers.teamx.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ViewPostActivity: ComponentActivity() {

    private var savedPost:SavedParcelable?=null
    private var postId:String?=null
    private val vm by viewModels<ViewPostViewModel>()
    private lateinit var adRequest:AdRequest

    @Inject lateinit var darkTheme: DarkTheme

    @OptIn(ExperimentalFoundationApi::class)
    @ExperimentalMaterialApi
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

                Scaffold(
                    topBar = {
                        ViewPostTopBar()
                    },
                    bottomBar = {
                        Banner{
                            it.loadAd(adRequest)
                        }
                    }
                ){
                    if(isSavedPost())
                        WebComposable(data = savedPost?.content?:"",darkTheme.isEnabled())
                    else{
                        Content()
                    }
                }

            }

        }
    }

    @Composable
    fun ViewPostTopBar(){


        if (isSavedPost()){

            val saved=vm.exists(savedPost?.postId!!).observeAsState(false)
            ViewPostTopBAr(
                saved=saved.value,
                onBackPressed = { finish() },
                onSaveChange = {
                    vm.onSave(it)
                }
            )

        }else{

            val saved=vm.exists(vm.post.value?.id?:"").observeAsState(false)
            ViewPostTopBAr(
                saved=saved.value,
                onBackPressed = { finish() },
                onSaveChange = {
                    vm.onSave(it)
                }
            )
        }
    }

    @ExperimentalFoundationApi
    @Composable
    private fun Content(){
        LazyColumn{

            item {
                vm.post.value?.let {post->
                    PostDetails(post)
                }
            }

            item{
                vm.post.value.let {post->
                    if(post==null)
                        CenterProgress()
                    else
                        WebComposable(data = post.content,darkTheme.isEnabled())
                }
            }

            stickyHeader {
                StickyHeader(text = "More Posts")
            }

            items(vm.posts){item->
                PostItem(item = item) {

                }
                Divider()
            }

        }
    }


    private fun isSavedPost()=savedPost!=null && vm.post.value==null && postId==null


}

