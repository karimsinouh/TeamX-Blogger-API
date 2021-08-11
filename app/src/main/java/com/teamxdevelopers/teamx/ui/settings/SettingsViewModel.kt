package com.teamxdevelopers.teamx.ui.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamxdevelopers.teamx.database.Database
import com.teamxdevelopers.teamx.utils.DarkTheme
import com.teamxdevelopers.teamx.utils.NotificationsUtility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val db:Database,
    private val notificationsUtility: NotificationsUtility,
    private val darkTheme: DarkTheme
):ViewModel() {

    var darkThemeEnabled:MutableState<Boolean> = mutableStateOf(false)

    init {
        darkThemeEnabled.value=darkTheme.isEnabled()
    }

    //notifications
    val isEnabled= mutableStateOf(notificationsUtility.isEnabled())

    val dialogState= mutableStateOf(false)

    fun setEnabled(value:Boolean){
        notificationsUtility.setEnabled(value)
    }

    fun clearSaved() =viewModelScope.launch{
        dialogState.value=false
        db.saved().clear()
    }

    fun setDarkTheme(boolean:Boolean){
        darkThemeEnabled.value=boolean
        darkTheme.setValue(boolean)
    }

}