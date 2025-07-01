package com.jurobil.materiapp.ui.screens.homeScreen.tabs.configuracionScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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

        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = innerPadding.calculateTopPadding() + 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                ConfigOption(
                    icon = Icons.Default.AccountCircle,
                    title = "Perfil",
                    onClick = { navController.navigate("ajustes_perfil") }
                )
            }
            item {
                ConfigOption(
                    icon = Icons.Default.Notifications,
                    title = "Notificaciones",
                    onClick = { navController.navigate("ajustes_notificaciones") }
                )
            }
            item {
                ConfigOption(
                    icon = Icons.Default.DarkMode,
                    title = "Apariencia",
                    onClick = { navController.navigate("ajustes_apariencia") }
                )
            }
            item {
                ConfigOption(
                    icon = Icons.Default.Info,
                    title = "Acerca de la App",
                    onClick = { navController.navigate("ajustes_acerca") }
                )
            }
            item {
                ConfigOption(
                    icon = Icons.Default.HelpOutline,
                    title = "Ayuda",
                    onClick = { navController.navigate("ajustes_ayuda") }
                )
            }
        }
    }
}


@Composable
fun ConfigOption(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}