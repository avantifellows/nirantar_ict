package com.avantifellows.nirantar.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.avantifellows.nirantar.viewmodels.LoginViewModel


@Composable
fun LoginPage(onContinueClicked: (String) -> Unit, loginVm: LoginViewModel) {
    val teacherIdValidationState by loginVm.teacherIDValidationState.collectAsState()

    if (teacherIdValidationState.isValid) {
        Log.d("YO", "You can log in now!")
        onContinueClicked(teacherIdValidationState.teacherID)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Nirantar!",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Please enter your teacher ID",
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = loginVm.teacherId,
            onValueChange = { loginVm.updateTeacherId(it) },
            label = { Text("Teacher ID") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            isError = loginVm.errorMessage.isEmpty(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Black,
                unfocusedBorderColor = Gray)
        )

        Box(modifier = Modifier.height(24.dp)) {
            Log.d("YO", "Button in button outside if!")
            Log.d("YO", "Button in button outside if ${teacherIdValidationState.isValid}!")
            if (!teacherIdValidationState.isValid) {
                Log.d("YO", "Button in button!")
                Text(
                    text = loginVm.errorMessage,
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        Button(
            onClick = {
                loginVm.validateTeacherId(loginVm.teacherId, onContinueClicked)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun DefaultPreview() {
    LoginPage(onContinueClicked = {}, loginVm = LoginViewModel())
}