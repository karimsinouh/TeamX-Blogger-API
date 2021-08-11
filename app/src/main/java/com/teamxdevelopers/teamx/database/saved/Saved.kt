package com.teamxdevelopers.teamx.database.saved

import android.content.Context
import android.content.Intent
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teamxdevelopers.teamx.ui.viewPost.ViewPostActivity

@Entity
data class Saved(
    val title:String,
    val thumbnail:String,
    val postId:String,
    val published:String,
    val content:String,
    @PrimaryKey(autoGenerate = true) val id:Int,
){

    fun view(c: Context,parcel:SavedParcelable?=null){
        val intent= Intent(c, ViewPostActivity::class.java)

        if(parcel!=null)
            intent.putExtra("savedPost",parcel)
        else
            intent.putExtra("postId",postId)

        c.startActivity(intent)
    }

    fun toParcelable():SavedParcelable{
        return SavedParcelable(
            title,
            thumbnail,
            postId,
            published,
            content,
            id
        )
    }

}