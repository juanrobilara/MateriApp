package com.jurobil.materiapp.ui.screens

import android.app.Activity
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.jurobil.materiapp.R
import com.jurobil.materiapp.data.network.GoogleAuthUiClient
import com.jurobil.materiapp.ui.viewmodel.LoginViewModel
import kotlinx.coroutines.coroutineScope
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "")
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = viewModel.email.value,
            onValueChange = viewModel::onEmailChanged,
            placeholder = { Text("Ingresa tu usuario") },
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(topStart = 12.dp, bottomEnd = 8.dp)),
            shape = RoundedCornerShape(topStart = 12.dp, bottomEnd = 8.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        PasswordTextField(
            text = viewModel.password.value,
            onTextChange = viewModel::onPasswordChanged
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.login() },
            modifier = Modifier
            .width(250.dp)
            .height(40.dp)) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("register") },
            modifier = Modifier
                .width(250.dp)
                .height(40.dp)) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            coroutineScope.launch {
                val intentSender = googleAuthUiClient.signIn()
                intentSender?.let {
                    launcher.launch(IntentSenderRequest.Builder(it).build())
                }
            } }, modifier = Modifier
            .width(250.dp)
            .height(40.dp),

        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "sign with google",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )

            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))

            Text("Ingresa con Google")
        }

    }

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
                LoginViewModel.LoginResult.Loading -> {

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
        placeholder = { Text("Ingresa tu contraseña") },
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(topStart = 12.dp, bottomEnd = 8.dp)),
        shape = RoundedCornerShape(topStart = 12.dp, bottomEnd = 8.dp),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Default.Check
            else Icons.Default.Clear

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña")
            }
        },
        singleLine = true
    )
}


@Preview
@Composable
fun LoginScreenPreview() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp)
            .background(Color.Yellow),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        var textValue by remember { mutableStateOf("Username") }
        var password by remember { mutableStateOf("") }

        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "")
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = textValue,
            onValueChange = {
                textValue = it
            },
            Modifier.background(Color.White, shape = RoundedCornerShape(topStart = 12.dp, bottomEnd = 8.dp)),
            shape = RoundedCornerShape(topStart = 12.dp, bottomEnd = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordTextField (
            "password",
            onTextChange = { password = it }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {},
            modifier = Modifier
                .width(250.dp)
                .height(40.dp)
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {},
            modifier = Modifier
                .width(250.dp)
                .height(40.dp)

        ) {
            Text("Sign in with Google")
        }
    }
}