package com.jurobil.materiapp.ui.screens

import android.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jurobil.materiapp.ui.viewmodel.RegisterViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(navController: NavHostController) {
    val viewModel: RegisterViewModel = hiltViewModel()
    val email = viewModel.email.value
    val password = viewModel.password.value
    val name = viewModel.name.value
    val registerResult = viewModel.registerResult
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var confirmEmail by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val emailMatchError = email.isNotBlank() && confirmEmail.isNotBlank() && email != confirmEmail
    val passwordMatchError = password.isNotBlank() && confirmPassword.isNotBlank() && password != confirmPassword

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(registerResult) {
        registerResult.collectLatest { result ->
            when (result) {
                is RegisterViewModel.RegisterResult.Loading -> {
                    isLoading = true
                    errorMessage = null
                }

                is RegisterViewModel.RegisterResult.Success -> {
                    isLoading = false
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                }

                is RegisterViewModel.RegisterResult.Error -> {
                    isLoading = false
                    errorMessage = result.errorMessage
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Registro", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = viewModel::onNameChanged,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = viewModel::onEmailChanged,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmEmail,
            onValueChange = { confirmEmail = it },
            label = { Text("Confirma tu Email") },
            isError = emailMatchError,
            modifier = Modifier.fillMaxWidth()
        )
        if (emailMatchError) {
            Text(
                text = "El email no coincide.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.Start)
            )
        }
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = viewModel::onPasswordChanged,
            label = { Text("Contraseña") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),

            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Default.Check
                else Icons.Default.Clear

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña")
                }
            }

        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirma tu contraseña") },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = passwordMatchError,
            modifier = Modifier.fillMaxWidth(),

            trailingIcon = {
                val image = if (confirmPasswordVisible)
                    Icons.Default.Check
                else Icons.Default.Clear

                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña")
                }
            }

        )
        if (passwordMatchError) {
            Text(
                text = "La contraseña no coincide.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.Start)
            )
        }
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (!emailMatchError && !passwordMatchError) {
                    viewModel.register()
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) "Registrando..." else "Registrarse")
        }

        if (errorMessage != null) {
            Spacer(Modifier.height(8.dp))
            ErrorMessage(errorMessage!!)
        }
    }
}

@Composable
fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.fillMaxWidth()
    )
}



@Composable
fun ErrorMessage() {

    Column (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text("Error al cargar usuario.")
    }
}