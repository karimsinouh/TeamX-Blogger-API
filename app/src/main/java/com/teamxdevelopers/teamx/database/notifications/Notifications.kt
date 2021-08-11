package com.teamxdevelopers.teamx.database.notifications

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Notifications (
    val title:String,
    val thumbnail:String,
    val postId:String,
    val seen:Boolean,
    @PrimaryKey(autoGenerate = true) val id:Int,
){

    companion object{
        fun extractFromMap(map:Map<String,String>):Notifications{
            return Notifications(
                map["title"].toString(),
                map["thumbnail"].toString(),
                map["postId"].toString(),
                false,
                0
            )
        }
    }

}