package com.teamxdevelopers.teamx.ui.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.rememberCoilPainter
import com.teamxdevelopers.teamx.R
import com.teamxdevelopers.teamx.data.Post
import com.teamxdevelopers.teamx.ui.notifications.NotificationsActivity
import com.teamxdevelopers.teamx.ui.saved.SavedActivity


@Composable
fun NotificationsActiveIcon(){
    Box(contentAlignment = Alignment.Center) {
       Icon(imageVector = Icons.Outlined.Notifications, contentDescription = null)
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
                .align(Alignment.TopEnd),
        )
    }
}


@Composable
fun TopBar(
    unSeenNotifications:Boolean,
    onNavigation:()->Unit,
) {

    val context= LocalContext.current

    TopAppBar(
        title={ Text(stringResource(R.string.app_name)) },
        actions = {
            IconButton(onClick = {
                NotificationsActivity.open(context)
            }) {
                if(unSeenNotifications)
                    NotificationsActiveIcon()
                else
                    Icon(Icons.Outlined.Notifications,null)
            }

            IconButton(onClick = {
                SavedActivity.open(context)
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_bookmark_outlined),null)
            }
        },
        navigationIcon = {
            IconButton(onNavigation) {
                Icon(Icons.Default.Menu,null)
            }
        },
        contentColor = MaterialTheme.colors.onBackground,
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    )

}

@Composable
fun SearchBar(
    value:String,
    onValueChange:(String)->Unit,
    onSearch:()->Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .background(MaterialTheme.colors.onBackground.copy(alpha = 0.1f))
    )
    {

        BasicTextField(
            value = value,
            onValueChange = {
                            onValueChange(it)
            },
            singleLine = true,
            modifier= Modifier
                .weight(0.9f)
                .padding(12.dp, 8.dp),
            textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
            cursorBrush = SolidColor(MaterialTheme.colors.primary)
        )

        IconButton(
            onClick = onSearch,
            modifier = Modifier
        ) {
            Icon(Icons.Outlined.Search, null)
        }

    }
}

@Composable
fun BLogPost(
    post:Post,
    onClick:()->Unit
){
    val imagePainter= rememberCoilPainter(request = post.getThumbnail())

    Column(
        modifier= Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = imagePainter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .height(190.dp)
                .fillMaxWidth(),
        )

        Text(
            text=post.title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text=post.published,
            color=MaterialTheme.colors.onBackground.copy(alpha = 0.8f),
            fontSize = 12.sp
        )

    }
}

@Composable
fun StickyHeader(text:String) {
    Text(
        text=text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(12.dp)
    )
}

@Composable
fun PostPlaceholder(){

    val color= MaterialTheme.colors.onBackground.copy(alpha = 0.1f)

    Column(
        modifier=Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color)
            .fillMaxWidth()
            .height(180.dp),)
        Box(modifier = Modifier
            .background(color)
            .width(300.dp)
            .height(25.dp),)
        Box(modifier = Modifier
            .background(color)
            .width(150.dp)
            .height(25.dp),)

    }
}

@Composable
fun PostPager(
    post:Post,
    onClick: () -> Unit
){

    val painter= rememberCoilPainter(request = post.getThumbnail())

    Box(
        modifier= Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ){

        Image(
            painter = painter,
            contentDescription =null,
            modifier=Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
        )

        Text(
            text =post.title,
            modifier= Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(12.dp),
            color=Color.White,
            fontSize = 18.sp
        )

    }
}

@ExperimentalMaterialApi
@Composable
fun MainDrawer(
    onSearch:()->Unit,
    onNotifications:()->Unit,
    onSaved:()->Unit,
    onSettings:()->Unit,
){
    Column(
        modifier= Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        ListItem(
            text={Text("Home",color = MaterialTheme.colors.primary)},
            icon = { Icon(Icons.Outlined.Home,null,tint=MaterialTheme.colors.primary) },
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.primary.copy(alpha = 0.2f))
        )

        ListItem(
            text={Text("Search")},
            icon = { Icon(Icons.Outlined.Search,null) },
            modifier = Modifier.clickable(onClick = onSearch)
        )

        ListItem(
            text={Text("Notifications")},
            icon = { Icon(Icons.Outlined.Notifications,null) },
            modifier = Modifier.clickable(onClick = onNotifications)
        )

        ListItem(
            text={Text("Saved")},
            icon = { Icon(painter = painterResource(id = R.drawable.ic_bookmark_outlined),null) },
            modifier = Modifier.clickable(onClick = onSaved)
        )

        ListItem(
            text={Text("Settings")},
            icon = { Icon(Icons.Outlined.Settings,null) },
            modifier = Modifier.clickable(onClick = onSettings)
        )
    }
}