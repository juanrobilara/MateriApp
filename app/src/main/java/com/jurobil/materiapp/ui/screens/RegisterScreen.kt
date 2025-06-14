package com.jurobil.materiapp.ui.screens

import android.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jurobil.materiapp.ui.theme.BackgroundColor
import com.jurobil.materiapp.ui.theme.CardColor
import com.jurobil.materiapp.ui.theme.PrimaryColor
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = CardColor)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = viewModel::onNameChanged,
                    label = { Text("Nombre completo") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = viewModel::onEmailChanged,
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmEmail,
                    onValueChange = { confirmEmail = it },
                    label = { Text("Confirmar correo") },
                    isError = emailMatchError,
                    modifier = Modifier.fillMaxWidth()
                )
                if (emailMatchError) {
                    Text("Los correos no coinciden", color = MaterialTheme.colorScheme.error)
                }

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = viewModel::onPasswordChanged,
                    label = { Text("Contraseña") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Clear else Icons.Default.Check
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(image, contentDescription = "Ver contraseña")
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar contraseña") },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = passwordMatchError,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        val image = if (confirmPasswordVisible) Icons.Default.Clear else Icons.Default.Check
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(image, contentDescription = "Ver contraseña")
                        }
                    }
                )
                if (passwordMatchError) {
                    Text("Las contraseñas no coinciden", color = MaterialTheme.colorScheme.error)
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (!emailMatchError && !passwordMatchError) {
                            viewModel.register()
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                ) {
                    Text(if (isLoading) "Registrando..." else "Registrarse", color = androidx.compose.ui.graphics.Color.White)
                }

                if (errorMessage != null) {
                    Spacer(Modifier.height(8.dp))
                    ErrorMessage(errorMessage!!)
                }
            }
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

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    // Simulación de datos locales
    var email by remember { mutableStateOf("usuario@email.com") }
    var password by remember { mutableStateOf("123456") }
    var name by remember { mutableStateOf("Juan Pérez") }
    var confirmEmail by remember { mutableStateOf("usuario@email.com") }
    var confirmPassword by remember { mutableStateOf("123456") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val emailMatchError = email != confirmEmail
    val passwordMatchError = password != confirmPassword

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = CardColor)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre completo") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmEmail,
                    onValueChange = { confirmEmail = it },
                    label = { Text("Confirmar correo") },
                    isError = emailMatchError,
                    modifier = Modifier.fillMaxWidth()
                )
                if (emailMatchError) {
                    Text("Los correos no coinciden", color = MaterialTheme.colorScheme.error)
                }

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Clear else Icons.Default.Check
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(image, contentDescription = "Ver contraseña")
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar contraseña") },
                    isError = passwordMatchError,
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        val image = if (confirmPasswordVisible) Icons.Default.Clear else Icons.Default.Check
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(image, contentDescription = "Ver contraseña")
                        }
                    }
                )
                if (passwordMatchError) {
                    Text("Las contraseñas no coinciden", color = MaterialTheme.colorScheme.error)
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { /* Preview, no acción */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                ) {
                    Text("Registrarse", color = androidx.compose.ui.graphics.Color.White)
                }
            }
        }
    }
}