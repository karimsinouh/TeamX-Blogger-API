package com.teamxdevelopers.teamx.ui.viewPost

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamxdevelopers.teamx.api.PostsEndPoint
import com.teamxdevelopers.teamx.data.Post
import com.teamxdevelopers.teamx.database.Database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewPostViewModel @Inject constructor(
    private val api: PostsEndPoint,
    private val database: Database
):ViewModel() {

    val post= mutableStateOf<Post?>(null)

    suspend fun loadPost(postId:String){
        api.getPost(postId){
            post.value=it
        }
    }

    fun exists(postId:String)=database.saved().exists(postId)

    private fun add()= viewModelScope.launch{
        database.saved().add(post.value?.asSavedPost()!!)
    }

    private fun remove()=viewModelScope.launch {
        database.saved().remove(post.value?.id!!)
    }

    fun onSave(it: Boolean) {
        if (it)
            add()
        else
            remove()
    }

}