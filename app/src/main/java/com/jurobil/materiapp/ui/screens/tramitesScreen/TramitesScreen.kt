package com.jurobil.materiapp.ui.screens.tramitesScreen

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
fun TramitesScreen(
    navController: NavHostController
) {
    MainScaffold(
        title = "Trámites",
        currentRoute = "tramites",
        onSignOut = {
            navController.navigate("login") {
                popUpTo("tramites") { inclusive = true }
            }
        },
        onNavigate = { route ->
            navController.navigate(route) {
                popUpTo("tramites") { inclusive = false }
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
            Text("Acá podrás gestionar tus trámites.")
        }
    }
}