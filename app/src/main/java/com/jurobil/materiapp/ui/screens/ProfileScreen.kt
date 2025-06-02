package com.jurobil.materiapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.jurobil.materiapp.ui.viewmodel.HomeViewModel

@Composable
fun ProfileScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    currentRoute: String = "profile",
    onNavigate: (String) -> Unit,
    onSignOut: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var photoUrl by remember { mutableStateOf<String?>(null) }
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getUserProfile { displayName, photo ->
            name = displayName ?: ""
            photoUrl = photo
        }
    }

    MainScaffold(
        title = "Perfil",
        currentRoute = currentRoute,
        onSignOut = onSignOut,
        onNavigate = onNavigate,
        showFab = false // Ocultar FAB en la pantalla de perfil
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            photoUrl?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .align(Alignment.CenterHorizontally)
                )
            }

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre de perfil") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.updateUserName(name) { success ->
                        showSuccess = success
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Guardar cambios")
            }

            if (showSuccess) {
                Spacer(Modifier.height(8.dp))
                Text("Nombre actualizado", color = Color.Green)
            }
        }
    }
}


@Composable
fun ProfileScreenWrapper(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    MainScaffold(
        title = "Perfil",
        currentRoute = "profile",
        onSignOut = {
            viewModel.signOut()
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        },
        onNavigate = { route ->
            navController.navigate(route) {
                popUpTo("home") { inclusive = false }
                launchSingleTop = true
            }
        },
        showFab = false
    ) { paddingValues ->
        ProfileScreenContent(viewModel, paddingValues)
    }
}

@Composable
fun ProfileScreenContent(
    viewModel: HomeViewModel,
    padding: PaddingValues
) {
    var name by remember { mutableStateOf("") }
    var photoUrl by remember { mutableStateOf<String?>(null) }
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getUserProfile { displayName, photo ->
            name = displayName ?: ""
            photoUrl = photo
        }
    }

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
            .fillMaxSize()
    ) {
        photoUrl?.let {
            AsyncImage(
                model = it,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .align(Alignment.CenterHorizontally)
            )
        }

        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre de perfil") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.updateUserName(name) { success ->
                    showSuccess = success
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Guardar cambios")
        }

        if (showSuccess) {
            Spacer(Modifier.height(8.dp))
            Text("Nombre actualizado", color = Color.Green)
        }
    }
}