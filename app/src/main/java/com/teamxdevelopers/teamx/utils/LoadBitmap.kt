package com.teamxdevelopers.teamx.utils


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL


private fun loadBitmap(imageSource:String):Bitmap?= try {
    val connection=URL(imageSource).openConnection()
    connection.connectTimeout=3000
    connection.connect()
    val inputStream=connection.getInputStream()
    val bitmap:Bitmap? =BitmapFactory.decodeStream(inputStream)
    inputStream.close()
    bitmap
}catch (e:Exception){
    null
}


fun String.loadBitmap(listener:(Bitmap?)->Unit)= CoroutineScope(Dispatchers.IO).launch{
    listener(loadBitmap(this@loadBitmap))
}