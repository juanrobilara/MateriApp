package com.jurobil.materiapp.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _registerResult = MutableSharedFlow<RegisterResult>()
    val registerResult = _registerResult.asSharedFlow()

    fun onNameChanged(newName: String) {
        _name.value = newName
    }

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun register() {
        viewModelScope.launch {
            _registerResult.emit(RegisterResult.Loading)
            try {
                val result = auth.createUserWithEmailAndPassword(email.value, password.value).await()
                val user = result.user ?: throw Exception("No se pudo crear el usuario")


                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name.value)
                    .build()
                user.updateProfile(profileUpdates).await()

                val userMap = mapOf(
                    "uid" to user.uid,
                    "email" to email.value,
                    "name" to name.value
                )
                firestore.collection("users").document(user.uid).set(userMap).await()

                _registerResult.emit(RegisterResult.Success)
            } catch (e: Exception) {
                Log.e("REGISTER", "Error: ${e.message}", e)
                _registerResult.emit(RegisterResult.Error("Error al registrar: ${e.message}"))
            }
        }
    }

    sealed class RegisterResult {
        object Loading : RegisterResult()
        object Success : RegisterResult()
        data class Error(val errorMessage: String) : RegisterResult()
    }
}