package com.jurobil.materiapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun MateriasEnCursoScreen(
    navController: NavHostController
) {
    MainScaffold(
        title = "Materias en curso",
        currentRoute = "materias_curso",
        onSignOut = {
            navController.navigate("login") {
                popUpTo("materias_curso") { inclusive = true }
            }
        },
        onNavigate = { route ->
            navController.navigate(route) {
                popUpTo("materias_curso") { inclusive = false }
                launchSingleTop = true
            }
        },
        showFab = false
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Acá se mostrarán las materias en curso.")
        }
    }
}