package com.jurobil.materiapp.ui.screens.loginScreen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.jurobil.materiapp.R
import com.jurobil.materiapp.data.network.GoogleAuthUiClient
import com.jurobil.materiapp.ui.core.theme.BackgroundColor
import com.jurobil.materiapp.ui.core.theme.PrimaryColor
import com.jurobil.materiapp.ui.screens.loginScreen.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val oneTapClient = remember { Identity.getSignInClient(context) }
    val firebaseAuth = FirebaseAuth.getInstance()
    val googleAuthUiClient = remember {
        GoogleAuthUiClient(
            context = context,
            oneTapClient = oneTapClient,
            auth = firebaseAuth
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data ?: return@rememberLauncherForActivityResult
                coroutineScope.launch {
                    val user = googleAuthUiClient.signInWithIntent(intent)
                    viewModel.handleGoogleSignInResult(user)
                }
            }
        }
    )

    // Estado para animaciones
    var animated by remember { mutableStateOf(false) }

    // Efecto de animación al aparecer
    LaunchedEffect(Unit) {
        animated = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(PrimaryColor.copy(alpha = 0.1f), BackgroundColor),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Tarjeta con animación de entrada
        AnimatedVisibility(
            visible = animated,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 40 }),
            exit = fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .widthIn(max = 500.dp),
                shape = MaterialTheme.shapes.extraLarge,
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo con animación
                    AnimatedVisibility(
                        visible = animated
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(PrimaryColor.copy(alpha = 0.1f))
                                .border(
                                    width = 1.dp,
                                    color = PrimaryColor.copy(alpha = 0.3f),
                                    shape = CircleShape
                                )
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.icon),
                                contentDescription = null,
                                modifier = Modifier.size(60.dp),
                                colorFilter = ColorFilter.tint(PrimaryColor)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Bienvenido",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Inicia sesión para continuar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Campo de email
                    OutlinedTextField(
                        value = viewModel.email.value,
                        onValueChange = viewModel::onEmailChanged,
                        label = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo de contraseña
                    PasswordTextField(
                        text = viewModel.password.value,
                        onTextChange = viewModel::onPasswordChanged
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Enlace de olvidó contraseña
                    TextButton(
                        onClick = { /* navController.navigate("forgot_password") */ },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón de login
                    Button(
                        onClick = { viewModel.login() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryColor,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {

                        Text(
                            text = "Iniciar sesión",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )

                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Texto de registro
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "¿No tienes cuenta?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        TextButton(
                            onClick = { navController.navigate("register") }
                        ) {
                            Text(
                                text = "Regístrate",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Divisor
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )
                        Text(
                            text = "o",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón de Google
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                val intentSender = googleAuthUiClient.signIn()
                                intentSender?.let {
                                    launcher.launch(IntentSenderRequest.Builder(it).build())
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = MaterialTheme.shapes.large,
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_google),
                                contentDescription = "Google logo",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = "Continuar con Google",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }

    // Manejo de resultados del login
    LaunchedEffect(Unit) {
        viewModel.loginResult.collectLatest { result ->
            when (result) {
                is LoginViewModel.LoginResult.Success -> {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }

                is LoginViewModel.LoginResult.Error -> {
                    Toast.makeText(context, result.errorMessage, Toast.LENGTH_LONG).show()
                }

                LoginViewModel.LoginResult.Loading -> { /* Loading se muestra en el botón */
                }
            }
        }
    }
}

@Composable
fun PasswordTextField(
    text: String,
    onTextChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text("Contraseña") },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            )
        },
        trailingIcon = {
            IconButton(
                onClick = { passwordVisible = !passwordVisible },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    )
}