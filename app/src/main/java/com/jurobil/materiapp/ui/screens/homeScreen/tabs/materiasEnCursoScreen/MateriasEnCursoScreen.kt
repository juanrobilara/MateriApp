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
    val tealPrimary = TealPrimary

    val backgroundEnCurso = MaterialTheme.colorScheme.primaryContainer
    val textEnCurso = MaterialTheme.colorScheme.primary
    val backgroundFinalizadas = MaterialTheme.colorScheme.surface
    val textFinalizadas = tealPrimary
    val backgroundPendientes = MaterialTheme.colorScheme.surface
    val textPendientes = warningYellow

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
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionMaterias(uiState.asignaturasEnCurso, "En curso", backgroundEnCurso, textEnCurso)
            SectionMaterias(uiState.asignaturasFinalizadas, "Finalizadas", backgroundFinalizadas, textFinalizadas)
            SectionMaterias(uiState.asignaturasPendientes, "Pendientes", backgroundPendientes, textPendientes)
        }
    }
}

private fun LazyListScope.SectionMaterias(
    lista: List<AsignaturaNew>,
    titulo: String,
    backgroundColor: Color,
    textColor: Color
) {
    if (lista.isNotEmpty()) {
        item {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White // Título siempre en blanco
            )
        }
        items(lista) {
            AsignaturaCard(it, backgroundColor, textColor)
        }
    }
}

@Composable
fun AsignaturaCard(asignatura: AsignaturaNew, backgroundColor: Color, textColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, textColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(asignatura.nombre, style = MaterialTheme.typography.titleMedium, color = textColor)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Inicio: ${asignatura.fechaInicio}")
                Text("Fin: ${asignatura.fechaFin}")
            }
            Spacer(Modifier.height(4.dp))
            Text(asignatura.estado.name, color = textColor)
            if (asignatura.observaciones.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text("Observaciones: ${asignatura.observaciones}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

