package com.teamxdevelopers.teamx.ui.saved

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamxdevelopers.teamx.database.Database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val db:Database
) :ViewModel() {

    val selected= mutableStateListOf<String>()

    val list=db.saved().getAllSavedPosts()

    fun isSelected(id:String):Boolean{
        return selected.contains(id)
    }

    fun deleteAllSelectedItems() =viewModelScope.launch{
        db.saved().deleteByList(selected)
        selected.clear()
    }

    fun onSelect(id:String, boolean: Boolean){
        if (boolean)
            selected.add(id)
        else
            selected.remove(id)
        Log.d("wtf","added/removed $id")
    }


}