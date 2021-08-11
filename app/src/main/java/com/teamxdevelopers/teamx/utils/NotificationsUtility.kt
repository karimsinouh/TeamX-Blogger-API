package com.teamxdevelopers.teamx.utils

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationsUtility @Inject constructor(
    @ApplicationContext private val context: Context
){

    private val prefs=context.getSharedPreferences("notifications",Context.MODE_PRIVATE)

    fun isEnabled():Boolean{
        return prefs.getBoolean("enabled",true)
    }

    fun setEnabled(value:Boolean){
        prefs.edit {
            putBoolean("enabled",value)
        }
    }

}