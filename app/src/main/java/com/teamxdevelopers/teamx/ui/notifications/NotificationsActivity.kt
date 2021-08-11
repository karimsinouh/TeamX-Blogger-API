package com.teamxdevelopers.teamx.ui.notifications

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.rememberCoilPainter
import com.teamxdevelopers.teamx.R
import com.teamxdevelopers.teamx.database.notifications.Notifications
import com.teamxdevelopers.teamx.ui.theme.BloggerAPITheme
import com.teamxdevelopers.teamx.ui.viewPost.ViewPostActivity
import com.teamxdevelopers.teamx.utils.DarkTheme
import com.teamxdevelopers.teamx.utils.MessageScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class NotificationsActivity:ComponentActivity() {

    companion object{
        fun open(c:Context){
            val intent=Intent(c,NotificationsActivity::class.java)
            c.startActivity(intent)
        }
    }

    private val vm by viewModels<NotificationsViewModel>()

    @Inject lateinit var darkTheme: DarkTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_BloggerAPI)
        setContent {

            val notifications=vm.notifications.observeAsState()

            BloggerAPITheme(darkTheme.isEnabled()) {
                window.statusBarColor=MaterialTheme.colors.background.toArgb()
                Surface(color = MaterialTheme.colors.background) {
                    Column(Modifier.fillMaxSize()) {

                        TopBar()

                        notifications.value?.let {
                            if (it.isEmpty())
                                MessageScreen(
                                    title = "0 Notifications",
                                    message ="You haven't received any notifications yet",
                                    button = "Got it",
                                    onClick = {finish()}
                                )
                            else
                                LazyColumn {
                                    items(it){ item->
                                        NotificationItem(post = item) {
                                            vm.makeAsSeen(item.id)
                                            openViewActivity(item.postId)
                                        }
                                        Divider()
                                    }
                                }
                        }

                    }
                }
            }
        }
    }

    @Composable
    @Preview
    private fun TopBar(){
        TopAppBar(
            title = { Text(text = "Notifications") },
            actions = {},
            navigationIcon = {
                IconButton(onClick = { finish() }) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            contentColor = MaterialTheme.colors.onBackground,
            backgroundColor = MaterialTheme.colors.background,
            elevation = 0.dp
        )
    }

    @Composable
    fun NotificationItem(
        post: Notifications,
        onClick: () -> Unit
    ){

        val painter= rememberCoilPainter(request =post.thumbnail)

        val background=if (!post.seen) MaterialTheme.colors.primary.copy(alpha = 0.1f) else Color.Transparent

        Row(
            modifier= Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .background(background)
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {


            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .height(100.dp)
                    .width(140.dp),
            )
            Column {
                Text(
                    text = "New Post",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text=post.title,
                    color=MaterialTheme.colors.onBackground.copy(alpha = 0.8f)
                )

            }

        }
    }

    private fun openViewActivity(id:String){
        val intent=Intent(this@NotificationsActivity,ViewPostActivity::class.java)
        intent.putExtra("postId",id)
        startActivity(intent)
    }


}