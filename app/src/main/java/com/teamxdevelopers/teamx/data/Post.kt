package com.teamxdevelopers.teamx.data

import android.content.Context
import android.content.Intent
import com.teamxdevelopers.teamx.database.notifications.Notifications
import com.teamxdevelopers.teamx.database.saved.Saved
import com.teamxdevelopers.teamx.ui.viewPost.ViewPostActivity

data class Post(
    val kind:String,
    val id:String,
    val published:String,
    val updated:String,
    val url:String,
    val title:String,
    val content:String,
    val images:List<PostImage>?=null,
){
    fun getThumbnail():String{
        return if (images!=null)
            images[0].url
        else
            "https://www.unfe.org/wp-content/uploads/2019/04/SM-placeholder-1024x512.png"
    }

    fun view(c:Context){
        val intent=Intent(c,ViewPostActivity::class.java)
        intent.putExtra("postId",id)
        c.startActivity(intent)
    }

    fun asSavedPost():Saved
    = Saved(title,getThumbnail(),id,published,content,0)

    fun asNotification():Notifications
    = Notifications(title,getThumbnail(),id,false,0)

}
