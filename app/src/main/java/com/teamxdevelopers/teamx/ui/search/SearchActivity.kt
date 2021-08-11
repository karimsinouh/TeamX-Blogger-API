package com.teamxdevelopers.teamx.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.teamxdevelopers.teamx.R
import com.teamxdevelopers.teamx.data.ScreenState
import com.teamxdevelopers.teamx.ui.main.PostPlaceholder
import com.teamxdevelopers.teamx.ui.main.SearchBar
import com.teamxdevelopers.teamx.ui.theme.BloggerAPITheme
import com.teamxdevelopers.teamx.utils.DarkTheme
import com.teamxdevelopers.teamx.utils.MessageScreen
import com.teamxdevelopers.teamx.utils.PostItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : ComponentActivity() {

    companion object{
        fun open(c:Context,q:String?=""){
            val intent=Intent(c,SearchActivity::class.java)
            q?.let {
                intent.putExtra("q",it)
            }
            c.startActivity(intent)
        }
    }

    private val vm by viewModels<SearchViewModel>()
    private lateinit var scope:CoroutineScope
    @Inject lateinit var darkTheme:DarkTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_BloggerAPI)

        setContent {

            intent.getStringExtra("q")?.let {
                if (it!=""){
                    vm.query.value=it
                    SideEffect {
                        scope.launch {
                            vm.search()
                        }
                    }
                }
            }

            scope= rememberCoroutineScope()
            BloggerAPITheme(darkTheme.isEnabled()) {
                window.statusBarColor=MaterialTheme.colors.background.toArgb()

                Scaffold(
                    topBar = { SearchSection() }
                ) {
                    when(vm.state.value){
                        ScreenState.IDLE->{
                            ContentSection()
                        }
                        ScreenState.LOADING->{
                            Column {
                                repeat(4){
                                    PostPlaceholder()
                                }
                            }
                        }

                        ScreenState.ERROR->{}
                    }
                }

            }
        }

    }

    @Composable
    private fun SearchSection() {
        Box(modifier= Modifier
            .fillMaxWidth()
            .padding(12.dp)){
            SearchBar(value = vm.query.value, onValueChange = {
                vm.query.value=it
            }) {
                scope.launch {
                    vm.search()
                }
            }
        }
    }

    @Composable
    private fun ContentSection(){

        vm.posts.value.let { posts->
            if (posts.isEmpty() && vm.serched){

                MessageScreen(
                    title = "No such result",
                    message ="We couldn't find any result that matches the input ${vm.query.value}",
                    button = "Got it",
                    onClick = { finish() }
                )

            }else{
                LazyColumn {
                    items(vm.posts.value){item->
                        PostItem(item){

                        }
                        Divider()
                    }
                }
            }
        }


    }

}

