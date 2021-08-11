package com.teamxdevelopers.teamx.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.teamxdevelopers.teamx.database.notifications.Notifications
import com.teamxdevelopers.teamx.database.notifications.NotificationsDao
import com.teamxdevelopers.teamx.database.saved.Saved
import com.teamxdevelopers.teamx.database.saved.SavedDao

@Database(entities = [Saved::class,Notifications::class],version = 1,exportSchema = false)
abstract class Database:RoomDatabase() {

    abstract fun saved():SavedDao

    abstract fun notifications():NotificationsDao
}