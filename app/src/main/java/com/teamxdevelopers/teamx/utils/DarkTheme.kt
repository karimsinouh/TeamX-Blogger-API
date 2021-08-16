package com.teamxdevelopers.teamx.utils

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DarkTheme @Inject constructor(
    @ApplicationContext private val context:Context
){

    private val prefs=context.getSharedPreferences("dark_prefs",Context.MODE_PRIVATE)

    fun isEnabled():Boolean{
        return prefs.getBoolean("enabled",true)
    }

    fun setValue(value:Boolean){
        prefs.edit {
            putBoolean("enabled",value)
        }
    }

}