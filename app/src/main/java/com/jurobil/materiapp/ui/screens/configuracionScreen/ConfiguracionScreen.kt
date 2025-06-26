package com.jurobil.materiapp.ui.screens.configuracionScreen

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
fun ConfiguracionScreen(
    navController: NavHostController
) {
    MainScaffold(
        title = "Configuración",
        currentRoute = "configuracion",
        onSignOut = {
            navController.navigate("login") {
                popUpTo("configuracion") { inclusive = true }
            }
        },
        onNavigate = { route ->
            navController.navigate(route) {
                popUpTo("configuracion") { inclusive = false }
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
            Text("Acá podrás configurar la aplicación.")
        }
    }
}