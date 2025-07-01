package com.jurobil.materiapp.ui.screens.homeScreen.tabs.materiasEnCursoScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
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
            section(uiState.asignaturasEnCurso, "En curso", Color.Blue)
            section(uiState.asignaturasFinalizadas, "Finalizadas", Color.Green)
            section(uiState.asignaturasPendientes, "Pendientes", Color.Gray)
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


@Composable
fun AsignaturaCard(asignatura: AsignaturaNew, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, color),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(asignatura.nombre, style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Inicio: ${asignatura.fechaInicio}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text("Fin: ${asignatura.fechaFin}", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(4.dp))

            when (asignatura.estado) {
                EstadoAsignatura.EN_CURSO -> Text("En curso", color = Color.Blue)
                EstadoAsignatura.FINALIZADA ->
                    Text("Finalizada - Nota: ${asignatura.nota}", color = Color.Green)

                EstadoAsignatura.PENDIENTE -> Text("Pendiente", color = Color.Gray)
            }

            if (asignatura.observaciones.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Observaciones: ${asignatura.observaciones}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

