package com.teamxdevelopers.teamx.api

import android.content.Context
import android.util.Log
import com.teamxdevelopers.teamx.api.ApiConstants.API_KEY
import com.teamxdevelopers.teamx.api.ApiConstants.BASE_URL
import com.teamxdevelopers.teamx.data.Post
import com.teamxdevelopers.teamx.data.ResponsePage
import com.teamxdevelopers.teamx.utils.ConnectivityUtility
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsEndPoint @Inject constructor(
    private val client:HttpClient,
    @ApplicationContext private val context: Context
    ) {

    suspend fun getPosts(pageToken:String?="",listener:(ResponsePage<Post>)->Unit){
        val url="${BASE_URL}posts?key=$API_KEY&fetchImages=true${
            if(pageToken!="") "&pageToken=$pageToken" else ""
        }"

        if (ConnectivityUtility.hasInternet(context))
            try{
                val response=client.get<ResponsePage<Post>>(url)
                listener(response)
            }catch (e:IOException){
                Log.d("PostsEndPoint",e.message!!)
            }catch(e: ClientRequestException){
                Log.d("PostsEndPoint",e.message!!)
            }

    }

    suspend fun getThreePosts(listener:(List<Post>)->Unit){
        val url="${BASE_URL}posts?key=$API_KEY&fetchImages=true&maxResults=3"

        if (ConnectivityUtility.hasInternet(context))
            try{
                val response=client.get<ResponsePage<Post>>(url)
                listener(response.items?: emptyList())
            }catch (e:IOException){
                Log.d("PostsEndPoint",e.message!!)
            }catch(e: ClientRequestException){
                Log.d("PostsEndPoint",e.message!!)
            }

    }

    suspend fun search(q:String,listener: (List<Post>) -> Unit){
        val url="${BASE_URL}posts/search?q=$q&key=$API_KEY&fetchImages=true"

        try{
            val response=client.get<ResponsePage<Post>>(url)
            listener(response.items?: emptyList())
        }catch (e:IOException){
            Log.d("PostsEndPoint",e.message!!)
        }catch(e: ClientRequestException){
            Log.d("PostsEndPoint",e.message!!)
        }
    }

    suspend fun getPost(postId:String,listener:(Post)->Unit){
        val url="${BASE_URL}posts/$postId?key=$API_KEY&fetchImages=true"

        try{
            val response=client.get<Post>(url)
            listener(response)
        }catch (e:IOException){
            Log.d("PostsEndPoint",e.message!!)
        }catch(e: ClientRequestException){
            Log.d("PostsEndPoint",e.message!!)
        }
    }

}