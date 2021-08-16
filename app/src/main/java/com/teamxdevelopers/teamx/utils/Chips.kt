package com.teamxdevelopers.teamx.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamxdevelopers.teamx.ui.theme.BloggerAPITheme

@Composable
fun Chip(text:String,colored: Boolean=false,onClick:(String)->Unit){

    val contentColor=if (colored) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
    val backgroundColor=if (colored) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.1f)

    Text(
        text = text,
        color = contentColor,
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(backgroundColor)
            .padding(4.dp)
            .clickable { onClick(text) }
    )

}

@Composable
fun ChipsList(chips:List<String>,colored:Boolean=false,onChipClick:(String)->Unit) {
    val scrollState= rememberScrollState()
    Row(
        modifier = Modifier.horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        chips.forEach { chip->
            Chip(
                text = chip,
                colored = colored,
                onClick = onChipClick
            )
        }
    }
}

@Preview()
@Composable
private fun ChipsListPreview(){
        ChipsList(
            chips = listOf("Anime","Tv Shows","Movies","Comic Books"),
            colored=false,
            onChipClick = {}
        )

}