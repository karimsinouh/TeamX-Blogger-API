package com.teamxdevelopers.teamx.database.saved

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SavedDao {

    @Query("SELECT * FROM Saved ORDER BY id DESC")
    fun getAllSavedPosts():LiveData<List<Saved>>

    @Insert(entity = Saved::class)
    suspend fun add(post:Saved)

    @Query("DELETE FROM Saved WHERE postId=:postId")
    suspend fun remove(postId:String)

    @Query("SELECT EXISTS (SELECT 1 FROM Saved WHERE postId=:postId ) ")
    fun exists(postId:String):LiveData<Boolean>

    @Query("DELETE FROM Saved WHERE postId in (:ids)")
    suspend fun deleteByList(ids:List<String>)

    @Query("DELETE FROM Saved")
    suspend fun clear()
}