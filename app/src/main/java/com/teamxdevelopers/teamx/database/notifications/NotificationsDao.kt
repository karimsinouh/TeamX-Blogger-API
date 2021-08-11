package com.teamxdevelopers.teamx.database.notifications

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotificationsDao {

    @Query("SELECT * FROM Notifications ORDER BY id DESC")
    fun getAllNotifications():LiveData<List<Notifications>>

    @Insert(entity = Notifications::class)
    fun add(item:Notifications)

    @Query("DELETE FROM Notifications WHERE id=:id")
    fun remove(id:String)

    @Query("UPDATE Notifications SET seen =:tr WHERE id=:id ")
    suspend fun makeAsSeen(id:Int,tr:Boolean=true)

}