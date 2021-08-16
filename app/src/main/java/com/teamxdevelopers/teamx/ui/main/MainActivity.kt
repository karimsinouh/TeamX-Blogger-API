package com.teamxdevelopers.teamx.ui.main

import android.os.Bundle
import com.teamxdevelopers.teamx.R
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.teamxdevelopers.teamx.data.ScreenState
import com.teamxdevelopers.teamx.ui.notifications.NotificationsActivity
import com.teamxdevelopers.teamx.ui.saved.SavedActivity
import com.teamxdevelopers.teamx.ui.search.SearchActivity
import com.teamxdevelopers.teamx.ui.settings.SettingsActivity
import com.teamxdevelopers.teamx.ui.theme.BloggerAPITheme
import com.teamxdevelopers.teamx.ui.theme.DrawerShape
import com.teamxdevelopers.teamx.utils.*
import com.teamxdevelopers.teamx.utils.ConnectivityUtility.hasInternet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var scaffoldState:ScaffoldState
    private lateinit var scope:CoroutineScope

    private val vm by viewModels<MainViewModel>()

    private lateinit var adRequest:AdRequest
    private var interstitial:InterstitialAd ?=null

    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_BloggerAPI)
        adRequest=AdRequest.Builder().build()

        setContent {

            scaffoldState= rememberScaffoldState()
            scope= rememberCoroutineScope()

            BloggerAPITheme(vm.darkThemeEnabled.value) {
                window.statusBarColor=MaterialTheme.colors.background.toArgb()

                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

                    Scaffold(
                        topBar = { TopBarSection() },
                        drawerContent = {DrawerContent()},
                        drawerShape = DrawerShape,
                        drawerContentColor = MaterialTheme.colors.onBackground,
                        drawerBackgroundColor = MaterialTheme.colors.background,
                        scaffoldState = scaffoldState,
                        bottomBar = {
                            Banner{
                                it.loadAd(adRequest)
                            }
                        }
                    )
                    {
                        //page content
                        if(hasInternet(this))
                            Content()
                        else
                            OfflineMessage()
                    }
                }
            }
        }

        Firebase.messaging.subscribeToTopic("general").addOnCompleteListener {
        }

        InterstitialAd.load(
            this,
            getString(R.string.interstitial),
            adRequest,
            object: InterstitialAdLoadCallback(){
                override fun onAdLoaded(p0: InterstitialAd) {
                    super.onAdLoaded(p0)
                    interstitial=p0
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        vm.setDarkTheme()
    }

    @Composable
    fun OfflineMessage() {
        MessageScreen(
            title = "Ops you're offline",
            message = "while waiting for the internet to come back, You can still view your saved posts in offline mode.",
            button = "Saved Posts",
            onClick = { SavedActivity.open(this) }
        )
    }

    @Composable
    fun TopBarSection() {

        val notifications=vm.notifications.observeAsState(emptyList())

        val unSeenNotifications=notifications.value.filter {
            !it.seen
        }

        Column {
            TopBar(unSeenNotifications.isNotEmpty()){
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }

            vm.categories.let {
                if (it.isNotEmpty())
                    CategoriesTabs()
            }
        }
    }

    @ExperimentalPagerApi
    @ExperimentalFoundationApi
    @Composable
    private fun Content(){
        LazyColumn {


            //search
            /*
            item {
                Box(modifier = Modifier.padding(8.dp)){
                    SearchBar(
                        value = vm.query.value,
                        onValueChange = {
                            vm.query.value=it
                        },
                        onSearch = {
                            SearchActivity.open(this@MainActivity,vm.query.value)
                        }
                    )
                }
            }

             */

            val state=vm.state.value

                if (state==ScreenState.LOADING && vm.pageToken==""){
                    items(5){
                        PostPlaceholder()
                    }
                    return@LazyColumn
                }

                if (state==ScreenState.IDLE || state==ScreenState.LOADING && vm.pageToken!=""){
                    vm.posts.let { posts->

                        if (posts.isNotEmpty()){

                            //stickyHeader { StickyHeader("Posts") }

                            item { Pager() }

                            //stickyHeader { StickyHeader(text = "Latest Posts") }


                            itemsIndexed(vm.filteredPosts()){index,item ->

                                PostItem(item,posts){
                                    interstitial?.show(this@MainActivity)
                                }
                                Divider()

                                //the end of the list has been reached
                                if ((index+1)==posts.size && vm.pageToken!=""){
                                    if(state!=ScreenState.LOADING){
                                        SideEffect {
                                            scope.launch {
                                                vm.loadPosts()
                                            }
                                        }
                                    }
                                }
                            }

                        }

                    }
                }

                if (state==ScreenState.LOADING && vm.pageToken!=""){
                    item {
                        CenterProgress(false)
                    }
                }


        }
    }

    @Composable
    private fun CategoriesTabs(){
        val selectedIndex=vm.selectedCategory.value
        ScrollableTabRow(
            selectedTabIndex = selectedIndex,
            contentColor = MaterialTheme.colors.onBackground,
            backgroundColor = MaterialTheme.colors.background,
            indicator = {position->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(position[selectedIndex]),
                    color = MaterialTheme.colors.primary
                )
            }
        ) {


            vm.categories.forEachIndexed{ index, category->
                val isSelected=selectedIndex==index
                val color=if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground
                Tab(selected = isSelected, onClick = {
                    vm.selectedCategory.value=index
                }) {
                    Text(text = category,modifier = Modifier.padding(12.dp),color=color)
                }
            }
        }
    }


    @ExperimentalPagerApi
    @Composable
    private fun Pager(){

        val pagerItems=vm.posts.getFive()
        val pagerState= rememberPagerState(pageCount = pagerItems.size)

        Column(modifier=Modifier.fillMaxWidth()) {

            HorizontalPager(
                state = pagerState,
                modifier=Modifier.fillMaxWidth()
            ) {page->
                val item=pagerItems[page]

                PostPager(item){
                    item.view(this@MainActivity)
                }

            }

            Indicators(
                size = pagerItems.size,
                index = pagerState.currentPage,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }

    @ExperimentalMaterialApi
    @Composable
    private fun DrawerContent(){
        MainDrawer(
            onSearch = {
                SearchActivity.open(this)
            },
            onNotifications = {
                NotificationsActivity.open(this)
            },
            onSaved = {
                SavedActivity.open(this)
            },
            onSettings = {
                SettingsActivity.open(this)
            }
            )
    }

}
