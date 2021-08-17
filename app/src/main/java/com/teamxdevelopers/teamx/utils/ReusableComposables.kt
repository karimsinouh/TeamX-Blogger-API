package com.teamxdevelopers.teamx.utils

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.rememberCoilPainter
import com.teamxdevelopers.teamx.data.Post
import com.teamxdevelopers.teamx.ui.main.BLogPost


@Composable
fun CenterProgress(
    fillMaxSize:Boolean=true
){

    val modifier=if (fillMaxSize)
        Modifier.fillMaxSize()
    else
        Modifier
            .fillMaxWidth()
            .padding(12.dp)

    Box(
        modifier= modifier,
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(strokeWidth = 3.dp)
    }
}


@Composable
fun Context.PostItem(
    item:Post,
    onClick: (() -> Unit)
){
    BLogPost(item){
        onClick()
        item.view(this)
    }

}

@Composable
fun BLogPostSmall(
    post:Post,
    onClick: () -> Unit
){

    val painter= rememberCoilPainter(request =post.getThumbnail())

    Row(
        modifier= Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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


@Composable
fun RoundedButton(
    text:String,
    onClick:()->Unit
) {
    Button(
        onClick = onClick,
        elevation = ButtonDefaults.elevation(0.dp,0.dp,0.dp),
        shape = CircleShape,
    ) {
        Text(text = text,fontWeight = FontWeight.Bold)
    }
}

@Composable
fun RoundedButtonOutlined(
    text:String,
    onClick:()->Unit
) {

    val colors=ButtonDefaults.buttonColors(
        backgroundColor = Color.Transparent,
        contentColor = MaterialTheme.colors.primary
    )

    Button(
        onClick = onClick,
        elevation = ButtonDefaults.elevation(0.dp,0.dp,0.dp),
        shape = CircleShape,
        colors = colors
    ) {
        Text(text = text,fontWeight = FontWeight.Bold)
    }
}

@Composable
fun MessageScreen(
    title:String,
    message:String,
    button:String?=null,
    onClick: (() -> Unit)?=null
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text =message,
            color = MaterialTheme.colors.onBackground.copy(alpha=0.8f),
            fontSize = 12.sp,
            modifier=Modifier.width(300.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))

        button?.let { buttonText->
            RoundedButton(buttonText){
                onClick!!()
            }
        }

    }
}

@Composable
@Preview
fun StickyHeaderText(){
    Row(modifier= Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.surface)
        .padding(12.dp)
    ){
        Text(text = "Sticky Header",fontSize=18.sp,fontWeight = FontWeight.Bold)
    }
}
