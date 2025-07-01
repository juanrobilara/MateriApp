package com.jurobil.materiapp.ui.screens.ajustesNotificacionesScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jurobil.materiapp.ui.screens.homeScreen.MainScaffold

@Composable
fun AjustesNotificacionesScreen(navController: NavHostController) {
    val examenAlertas = remember { mutableStateOf(true) }
    val cursosAlertas = remember { mutableStateOf(false) }
    val recordatoriosAlertas = remember { mutableStateOf(true) }

    MainScaffold(
        title = "Notificaciones",
        currentRoute = "ajustes_notificaciones",
        onNavigate = { navController.navigate(it) },
        onSignOut = { navController.navigate("login") },
        showFab = false
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NotificationSwitch(
                title = "Alertas de Exámenes",
                state = examenAlertas
            )
            NotificationSwitch(
                title = "Alertas de Cursos Nuevos",
                state = cursosAlertas
            )
            NotificationSwitch(
                title = "Recordatorios Generales",
                state = recordatoriosAlertas
            )
        }
    }
}

@Composable
fun NotificationSwitch(title: String, state: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = state.value,
            onCheckedChange = { state.value = it }
        )
    }
}
