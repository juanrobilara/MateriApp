package com.jurobil.materiapp.ui.screens.ajustesPerfilScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.jurobil.materiapp.ui.screens.homeScreen.MainScaffold

@Composable
fun AjustesPerfilScreen(navController: NavHostController) {
    MainScaffold(
        title = "Perfil",
        currentRoute = "ajustes_perfil",
        onNavigate = { navController.navigate(it) },
        onSignOut = { navController.navigate("login") },
        showFab = false
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Pantalla de ajustes de Perfil.")
        }
    }
}
