package com.teamxdevelopers.teamx.ui.rateDialog

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import com.teamxdevelopers.teamx.R
import com.teamxdevelopers.teamx.utils.RoundedButton
import com.teamxdevelopers.teamx.utils.RoundedButtonOutlined


@Composable
fun RateDialog(
    onRate:()->Unit,
    onExit:()->Unit,
    onDismiss:()->Unit
){
    Dialog(
        onDismissRequest = onDismiss,
    ){
        RateDialogUI(onRate,onExit)
    }
}


@Composable
fun RateDialogUI(
    onRate:()->Unit,
    onExit:()->Unit
) {

    val painter= rememberImagePainter(
        data=R.drawable.rateus,
        builder = {
            decoder(GifDecoder())
        }
    )

    Column(
        modifier = Modifier
            .padding(32.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(200.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Rate This Application",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Thank you so much for using our app, if you really like our app, please rate us 5 stars",
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            modifier=Modifier.width(200.dp),
            textAlign = TextAlign.Center,
            fontSize=12.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier=Modifier.padding(8.dp)) {
            RoundedButton(text ="Rate") {
                onRate()
            }
            Spacer(modifier = Modifier.width(12.dp))
            RoundedButtonOutlined(text ="Exit") {
                onExit()
            }
        }

    }

}