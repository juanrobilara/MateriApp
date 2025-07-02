package com.jurobil.materiapp.ui.screens.homeScreen.tabs.materiasEnCursoScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jurobil.materiapp.ui.core.theme.CompleteGreen
import com.jurobil.materiapp.ui.core.theme.TealPrimary
import com.jurobil.materiapp.ui.core.theme.WarningYellow
import com.jurobil.materiapp.ui.screens.homeScreen.MainScaffold
import com.jurobil.materiapp.ui.screens.homeScreen.tabs.materiasEnCursoScreen.model.AsignaturaNew
import com.jurobil.materiapp.ui.screens.homeScreen.tabs.materiasEnCursoScreen.model.EstadoAsignatura
import com.jurobil.materiapp.ui.screens.homeScreen.tabs.materiasEnCursoScreen.viewmodel.MateriasEnCursoViewModel

@Composable
fun MateriasEnCursoScreen(
    navController: NavHostController,
    viewModel: MateriasEnCursoViewModel = hiltViewModel()
) {
    val carreraId = navController.currentBackStackEntry?.arguments?.getString("carreraId") ?: "1"

    LaunchedEffect(carreraId) {
        viewModel.cargarAsignaturas(carreraId)
    }

    val uiState by viewModel.uiState.collectAsState()

    // Definición de colores dentro de un @Composable
    val warningYellow = WarningYellow
    val completeGreen = CompleteGreen
    val tealPrimary = TealPrimary

    MainScaffold(
        title = "Materias - ${uiState.carrera?.nombre ?: ""}",
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
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionMaterias(uiState.asignaturasEnCurso, "En curso", tealPrimary)
            SectionMaterias(uiState.asignaturasFinalizadas, "Finalizadas", completeGreen)
            SectionMaterias(uiState.asignaturasPendientes, "Pendientes", warningYellow)
        }
    }
}

private fun LazyListScope.SectionMaterias(lista: List<AsignaturaNew>, titulo: String, color: Color) {
    if (lista.isNotEmpty()) {
        item { Text(titulo, style = MaterialTheme.typography.titleMedium, color = color) }
        items(lista) { AsignaturaCard(it, color) }
    }
}

@Composable
fun AsignaturaCard(asignatura: AsignaturaNew, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, color),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(asignatura.nombre, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Inicio: ${asignatura.fechaInicio}")
                Text("Fin: ${asignatura.fechaFin}")
            }
            Spacer(Modifier.height(4.dp))
            Text(asignatura.estado.name, color = color)
            if (asignatura.observaciones.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text("Observaciones: ${asignatura.observaciones}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun AsignaturaSection(lista: List<AsignaturaNew>, titulo: String, color: Color) {
    if (lista.isNotEmpty()) {
        Text(titulo, style = MaterialTheme.typography.titleMedium, color = color)
        Spacer(Modifier.height(8.dp))
        lista.forEach { asignatura ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(8.dp).background(color, CircleShape))
                        Spacer(Modifier.width(8.dp))
                        Text(asignatura.nombre, style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(Modifier.height(4.dp))
                    Text("Inicio: ${asignatura.fechaInicio}  |  Fin: ${asignatura.fechaFin}", style = MaterialTheme.typography.bodySmall)
                    if (asignatura.observaciones.isNotEmpty()) {
                        Spacer(Modifier.height(4.dp))
                        Text("Observaciones: ${asignatura.observaciones}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

private fun LazyListScope.section(
    lista: List<AsignaturaNew>,
    titulo: String,
    color: Color
) {
    if (lista.isNotEmpty()) {
        item {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
        }
        items(lista) {
            AsignaturaCard(it, color)
        }
    }
}




