package com.avantifellows.nirantar.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginViewModel() : ViewModel() {
    private val _teacherIDValidationState = MutableStateFlow(LoginPageUIState(false, "", ""))
    val teacherIDValidationState: StateFlow<LoginPageUIState> = _teacherIDValidationState.asStateFlow()
    var teacherId: String by  mutableStateOf("")
        private set
    val errorMessage: String
        get() = _teacherIDValidationState.value.errorMessage

    fun validateTeacherId(
        teacherId: String,
        onContinueClicked: (String) -> Unit
    ) {

        Log.d("YO", "Checking Firestore")
        viewModelScope.launch {
            val db = Firebase.firestore
            val teachersCollection = db.collection("AFTeachers")
            val query = teachersCollection.whereEqualTo("teacher_id", teacherId)
            Log.d("YO", "Teacher id is $teacherId")
            val task = query.get()

            try {
                Log.d("YO", "Comparing things")

                withContext(Dispatchers.IO) {
                    val result = Tasks.await(task)
                    for (doc in result) {
                        Log.d("YO", "${doc.data}")
                    }
                    if (!result.isEmpty) {
                        _teacherIDValidationState.update { LoginPageUIState(true, "Teacher ID exists", teacherId) } // TeacherIdValidationResult(true, "Teacher ID found", "")

                        onContinueClicked(teacherId)
                    } else {
                        Log.d("YO", "NO RESULT NO TEACHER")
                        _teacherIDValidationState.update { LoginPageUIState(false, "Teacher ID does not exist", teacherId) }
                        Log.d("YO", _teacherIDValidationState.value.errorMessage)
                    }
                }

            } catch (e: Exception) {
                Log.e("YO LoginPage", "Error querying Firestore", e)
            }
        }
    }

    fun updateTeacherId(enteredTeacherId: String){
        teacherId = enteredTeacherId
    }

}

data class LoginPageUIState(val isValid: Boolean, val errorMessage: String, val teacherID: String)
