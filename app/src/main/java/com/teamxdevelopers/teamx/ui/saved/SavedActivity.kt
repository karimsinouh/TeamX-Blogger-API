package com.teamxdevelopers.teamx.ui.saved

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.coil.rememberCoilPainter
import com.teamxdevelopers.teamx.R
import com.teamxdevelopers.teamx.database.saved.Saved
import com.teamxdevelopers.teamx.ui.theme.BloggerAPITheme
import com.teamxdevelopers.teamx.utils.ConnectivityUtility
import com.teamxdevelopers.teamx.utils.DarkTheme
import com.teamxdevelopers.teamx.utils.MessageScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SavedActivity:ComponentActivity() {

    companion object{
        fun open(c:Context){
            val intent=Intent(c,SavedActivity::class.java)
            c.startActivity(intent)
        }
    }

    private val vm by viewModels<SavedViewModel>()
    private var selectionMode:Boolean=false
    private lateinit var scaffoldState: ScaffoldState

    @Inject lateinit var darkTheme: DarkTheme

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_BloggerAPI)

        setContent {

            val list=vm.list.observeAsState()
            selectionMode=vm.selected.isNotEmpty()
            scaffoldState= rememberScaffoldState()

            BloggerAPITheme(darkTheme.isEnabled()) {
                window.statusBarColor=MaterialTheme.colors.background.toArgb()
                Scaffold(
                    topBar = {
                        TopBar()
                    },
                    scaffoldState = scaffoldState
                ) {

                    list.value?.let {
                        if(it.isEmpty())

                            MessageScreen(
                                title = "No Saved Items",
                                message = "you haven't saved any posts yet",
                                button = "Got it",
                                onClick = { finish() }
                            )

                        else
                            LazyColumn {
                                items(it){item->
                                    SavedItem(
                                        post = item,
                                        onLongClick={
                                            vm.selected.add(item.postId)
                                        },
                                        onClick = {
                                            if (!selectionMode)
                                                viewPost(item)
                                        }
                                    )
                                    Divider()
                                }
                            }

                    }

                }
            }

        }

    }


    //@Preview
    @ExperimentalAnimationApi
    @Composable
    private fun TopBar(){
        TopAppBar(
            title={
                  Text(text = "Saved")
            },
            navigationIcon = {
                IconButton(onClick = {
                    finish()
                }) {
                    Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
                }
            },
            contentColor = MaterialTheme.colors.onBackground,
            backgroundColor = MaterialTheme.colors.background,
            elevation = 0.dp,
            actions = {
                AnimatedVisibility(visible = selectionMode) {
                    IconButton(onClick = {
                        lifecycleScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("${vm.selected.size} items have been deleted")
                        }
                        vm.deleteAllSelectedItems()
                    }) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                    }
                }
            }
        )
    }

    @ExperimentalAnimationApi
    @Composable
    fun SavedItem(
        post: Saved,
        onClick: () -> Unit,
        onLongClick:()->Unit,
    ){

        val selected=vm.isSelected(post.postId)

        val painter= rememberCoilPainter(request =post.thumbnail)

        Row(
            modifier= Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { onLongClick() },
                        onTap = { onClick() },
                    )
                }
                //.clickable(onClick = onClick)
                .padding(12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            AnimatedVisibility(visible = selectionMode) {

                    Checkbox(
                        checked = selected,
                        onCheckedChange = {
                            vm.onSelect(post.postId,it)
                        },
                        modifier= Modifier.padding(12.dp)
                    )

            }

            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .height(100.dp)
                    .width(140.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text=post.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 3
                )

                Text(
                    text=post.published,
                    color=MaterialTheme.colors.onBackground.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }

        }
    }


    private fun viewPost(item: Saved){
        if (ConnectivityUtility.hasInternet(this))
            item.view(this)
        else
            item.view(this,item.toParcelable())
    }

}