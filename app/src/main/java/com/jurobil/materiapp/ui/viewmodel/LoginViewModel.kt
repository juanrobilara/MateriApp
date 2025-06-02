package com.jurobil.materiapp.ui.viewmodel

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.jurobil.materiapp.data.network.GoogleAuthUiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _loginResult = MutableSharedFlow<LoginResult>()
    val loginResult = _loginResult.asSharedFlow()

    fun onEmailChanged(newEmail: String) { _email.value = newEmail }
    fun onPasswordChanged(newPassword: String) { _password.value = newPassword }

    fun login() {
        viewModelScope.launch {
            _loginResult.emit(LoginResult.Loading)
            try {
                auth.signInWithEmailAndPassword(email.value, password.value)
                    .addOnCompleteListener { task ->
                        viewModelScope.launch {
                            if (task.isSuccessful) {
                                _loginResult.emit(LoginResult.Success)
                            } else {
                                _loginResult.emit(LoginResult.Error(task.exception?.message ?: "Login failed"))
                            }
                        }
                    }
            } catch (e: Exception) {
                _loginResult.emit(LoginResult.Error(e.message ?: "Unexpected error"))
            }
        }
    }

    fun handleGoogleSignInResult(user: FirebaseUser?) {
        viewModelScope.launch {
            _loginResult.emit(LoginResult.Loading)
            if (user != null) {
                val userDoc = firestore.collection("users").document(user.uid)
                val snapshot = userDoc.get().await()
                if (!snapshot.exists()) {
                    userDoc.set(
                        mapOf(
                            "uid" to user.uid,
                            "email" to user.email,
                            "name" to user.displayName
                        )
                    )
                }
                _loginResult.emit(LoginResult.Success)
            } else {
                _loginResult.emit(LoginResult.Error("Google sign-in failed"))
            }
        }
    }

    sealed class LoginResult {
        object Loading : LoginResult()
        object Success : LoginResult()
        data class Error(val errorMessage: String) : LoginResult()
    }
}