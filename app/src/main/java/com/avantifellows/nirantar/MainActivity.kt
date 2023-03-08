package com.avantifellows.nirantar

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.avantifellows.nirantar.ui.screens.ContentFileList
import com.avantifellows.nirantar.ui.screens.LoginPage
import com.avantifellows.nirantar.ui.theme.MyApplicationTheme
import com.avantifellows.nirantar.viewmodels.ContentFileListViewModel
import com.avantifellows.nirantar.viewmodels.LoginViewModel
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this);

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

    if (!loggedIn) {
        LoginPage(onContinueClicked =  { teacherId: String ->
            sharedPref.edit().putBoolean("loggedIn", true).commit()
            sharedPref.edit().putString("teacherId", teacherId).commit()
            loggedIn = true
        }, loginVm)
    } else {
        ContentFileList(contentFileListVm)
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun DefaultPreview() {
    MyApplicationTheme() {
        ContentFileList(ContentFileListViewModel())
    }
}