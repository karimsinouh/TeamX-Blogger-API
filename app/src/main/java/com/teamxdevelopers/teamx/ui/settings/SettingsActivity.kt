package com.teamxdevelopers.teamx.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamxdevelopers.teamx.ui.theme.BloggerAPITheme
import dagger.hilt.android.AndroidEntryPoint
import com.teamxdevelopers.teamx.R
import com.teamxdevelopers.teamx.utils.DarkTheme
import com.teamxdevelopers.teamx.utils.RoundedButton
import com.teamxdevelopers.teamx.utils.RoundedButtonOutlined
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity:ComponentActivity() {

    companion object{
        fun open(c:Context){
            val intent= Intent(c,SettingsActivity::class.java)
            c.startActivity(intent)
        }
    }

    private val vm by viewModels<SettingsViewModel>()


    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_BloggerAPI)
        setContent {

            BloggerAPITheme(vm.darkThemeEnabled.value){

                window.statusBarColor=MaterialTheme.colors.background.toArgb()

                Surface(color=MaterialTheme.colors.background) {
                    Column(Modifier.fillMaxSize()) {
                        TopBar()
                        Content()
                    }

                    if (vm.dialogState.value)
                        AlertDialog(
                            onDismissRequest = {vm.dialogState.value=false},
                            title = {Text("Clear Saved")},
                            text = { Text(text = "Please confirm that you want to delete all saved items.")},
                            confirmButton = {
                                RoundedButton("Confirm") {
                                vm.clearSaved()
                            }},
                            dismissButton = {
                                RoundedButtonOutlined("Cancel") {
                                    vm.dialogState.value=false
                                }
                            },
                            modifier = Modifier.padding(32.dp)
                        )


                }
            }
        }

    }

    @Composable
    @Preview
    private fun TopBar(){
        TopAppBar(
            title ={ Text(text = "Settings")},
            navigationIcon = { IconButton(onClick = { finish() }) {
                Icon(Icons.Outlined.ArrowBack,null)
            }},
            contentColor = MaterialTheme.colors.onBackground,
            backgroundColor = MaterialTheme.colors.background,
            elevation = 0.dp,
        )
    }

    @ExperimentalMaterialApi
    @Composable
    @Preview
    private fun Content() {
        Column(
            modifier= Modifier.verticalScroll(rememberScrollState())
        ) {

            ListItem(
                text = { Text(text = "Dark Theme")},
                secondaryText = { Text(text = "Protect your eyes at night enabling dark theme\n")},
                icon = { Icon(painter= painterResource(id = R.drawable.ic_bedtime), null)},
                trailing = {
                    Checkbox(
                        checked = vm.darkThemeEnabled.value,
                        onCheckedChange = {
                            vm.setDarkTheme(it)
                        })
                }
            )
            Divider()

            ListItem(
                text = { Text(text = "Notifications")},
                secondaryText = { Text(text = "Enable receiving notifications")},
                icon = { Icon(Icons.Outlined.Notifications, null)},
                trailing = {
                    Checkbox(
                        checked = vm.isEnabled.value,
                        onCheckedChange = {
                            vm.isEnabled.value=it
                            vm.setEnabled(it)
                        })
                }
            )
            Divider()

            ListItem(
                text = { Text(text = "Clear Saved")},
                secondaryText = { Text(text = "Delete all saved items")},
                icon = { Icon(painterResource(id = R.drawable.ic_bookmark_outlined), null)},
                modifier=Modifier.clickable {
                    vm.dialogState.value=true
                }
           )
            Divider()
        }
    }

}