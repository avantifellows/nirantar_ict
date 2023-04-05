package com.avantifellows.nirantar

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.avantifellows.nirantar.ui.screens.HomeScreen
import com.avantifellows.nirantar.ui.screens.LoginPage
import com.avantifellows.nirantar.ui.theme.MyApplicationTheme
import com.avantifellows.nirantar.ui.theme.Teal200
import com.avantifellows.nirantar.ui.theme.Teal700
import com.avantifellows.nirantar.viewmodels.ContentFileListViewModel
import com.avantifellows.nirantar.viewmodels.LoginViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent


class MainActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this);
        analytics = FirebaseAnalytics.getInstance(this)

        setContent {
            MyApplicationTheme() {
                App()
            }
        }
    }
}

@Composable
private fun App() {
    val sharedPref = LocalContext.current.getSharedPreferences("app_state", Context.MODE_PRIVATE)
    var loggedIn by rememberSaveable { mutableStateOf(sharedPref.getBoolean("loggedIn", false)) }

    val contentFileListVm = ContentFileListViewModel()
    val loginVm = LoginViewModel()
    val analytics = FirebaseAnalytics.getInstance(LocalContext.current)

    if (!loggedIn) {
        LoginPage(onContinueClicked =  { teacherId: String ->
            sharedPref.edit().putBoolean("loggedIn", true).commit()
            sharedPref.edit().putString("teacherId", teacherId).commit()
            analytics.setUserId(teacherId)
            analytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                param(FirebaseAnalytics.Param.ITEM_NAME, "name")
                param(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
            }
            loggedIn = true
        }, loginVm)
    } else {
        Scaffold(
            bottomBar = {
                BottomAppBar(backgroundColor = Teal700) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(backgroundColor = Teal200, contentColor = Color.White),
                            onClick =  {
                                sharedPref.edit().remove("loggedIn").commit()
                                sharedPref.edit().remove("teacherId").commit()
                                loggedIn = false
                            },
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("Logout", color=Color.Gray)
                        }
                    }
                }
            }
        ) {
            HomeScreen(contentFileListVm)
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun DefaultPreview() {
    MyApplicationTheme() {
        HomeScreen(ContentFileListViewModel())
    }
}