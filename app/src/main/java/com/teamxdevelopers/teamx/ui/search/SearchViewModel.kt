package com.teamxdevelopers.teamx.ui.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.teamxdevelopers.teamx.api.PostsEndPoint
import com.teamxdevelopers.teamx.data.Post
import com.teamxdevelopers.teamx.data.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val api:PostsEndPoint
):ViewModel(){

    var serched=false
    val query= mutableStateOf("")
    val state= mutableStateOf(ScreenState.IDLE)
    val posts= mutableStateOf<List<Post>>(emptyList())

    suspend fun search(){
        if (query.value.isNotEmpty()){
            serched=true
            state.value=ScreenState.LOADING

            api.search(query.value){
                posts.value=it
                state.value=ScreenState.IDLE
            }
        }
    }

}