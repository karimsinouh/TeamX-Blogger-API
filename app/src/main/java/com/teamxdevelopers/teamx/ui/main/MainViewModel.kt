package com.teamxdevelopers.teamx.ui.main

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamxdevelopers.teamx.api.PostsEndPoint
import com.teamxdevelopers.teamx.data.Post
import com.teamxdevelopers.teamx.data.ScreenState
import com.teamxdevelopers.teamx.database.Database
import com.teamxdevelopers.teamx.utils.DarkTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val api:PostsEndPoint,
    private val db:Database,
    private val darkTheme: DarkTheme,
) :ViewModel() {

    var pageToken:String?=""
    val darkThemeEnabled= mutableStateOf(false)
    val query= mutableStateOf("")
    val posts= mutableStateListOf<Post>()
    val state= mutableStateOf(ScreenState.LOADING)
    val notifications= db.notifications().getAllNotifications()

    fun setDarkTheme(){
        darkThemeEnabled.value=darkTheme.isEnabled()
    }

    init {

        setDarkTheme()

        viewModelScope.launch {
            loadPosts()
        }
    }

    suspend fun loadPosts(){

        state.value=ScreenState.LOADING

        api.getPosts(pageToken){
            pageToken=it.nextPageToken?:""
            posts.addAll(it.items?: emptyList())
            state.value=ScreenState.IDLE
        }
    }

}