package com.teamxdevelopers.teamx.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamxdevelopers.teamx.database.Database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val db:Database
): ViewModel(){

    val notifications=db.notifications().getAllNotifications()

    fun makeAsSeen(id:Int)=viewModelScope.launch{
        db.notifications().makeAsSeen(id)
    }

}