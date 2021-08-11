package com.teamxdevelopers.teamx.utils

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun Indicator(
    selected:Boolean
){

    val color= if (selected) MaterialTheme.colors.primary
    else
        MaterialTheme.colors.onBackground.copy(alpha = 0.5f)

    val width= animateDpAsState(
        targetValue = if (selected) 25.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Box(
        modifier= Modifier
            .width(width.value)
            .height(10.dp)
            .clip(CircleShape)
            .background(color)
    )
}


@Composable
fun Indicators(
    size:Int,
    index:Int,
    modifier: Modifier = Modifier
){

    Row(
        modifier=modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        repeat(size){
            Indicator(it==index)
        }
    }

}