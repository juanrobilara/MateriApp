package com.jurobil.materiapp.ui.screens.homeScreen.tabs.tramitesScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jurobil.materiapp.ui.screens.homeScreen.MainScaffold

@OptIn(ExperimentalFoundationApi::class)
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

        val tramites = listOf(
            TramiteItem("Constancia", Icons.Default.Description),
            TramiteItem("Inscripción", Icons.Default.Edit),
            TramiteItem("Verificación", Icons.Default.CheckCircle),
            TramiteItem("Certificado", Icons.Default.AssignmentTurnedIn),
            TramiteItem("Analítico", Icons.Default.Download),
            TramiteItem("Boleto estudiantil", Icons.Default.DirectionsBus),
            TramiteItem("Calendario", Icons.Default.Event),
            TramiteItem("Más acciones", Icons.Default.MoreHoriz)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = innerPadding.calculateTopPadding() + 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(tramites) { tramite ->
                TramiteButton(
                    tramite = tramite,
                    onClick = {
                        // Aquí defines la navegación según el trámite
                        when (tramite.nombre) {
                            "Constancia" -> navController.navigate("constancia_screen")
                            "Inscripción" -> navController.navigate("inscripcion_screen")
                            "Verificación" -> navController.navigate("verificacion_screen")
                            "Certificado" -> navController.navigate("certificado_screen")
                            "Analítico" -> navController.navigate("analitico_screen")
                            "Boleto estudiantil" -> navController.navigate("boleto_screen")
                            "Calendario" -> navController.navigate("calendario_screen")
                            "Más acciones" -> navController.navigate("mas_acciones_screen")
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun TramiteButton(
    tramite: TramiteItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .clickable { onClick() }
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = tramite.icono,
            contentDescription = tramite.nombre,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = tramite.nombre,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

data class TramiteItem(
    val nombre: String,
    val icono: ImageVector
)



/*
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
}*/
