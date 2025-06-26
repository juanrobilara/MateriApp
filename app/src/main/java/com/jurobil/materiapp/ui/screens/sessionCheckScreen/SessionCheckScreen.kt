package com.jurobil.materiapp.ui.screens.sessionCheckScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SessionCheckScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            navController.navigate("home") {
                popUpTo("session") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("session") { inclusive = true }
            }
        }
    }

    // Puedes mostrar un loader opcionalmente
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}