package com.jurobil.materiapp.ui.screens.detalleCarreraScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jurobil.materiapp.domain.model.Carrera
import com.jurobil.materiapp.ui.core.theme.CompleteGreen
import com.jurobil.materiapp.ui.screens.homeScreen.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCarreraScreen(
    carreraId: String,
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController
) {
    var carrera by remember { mutableStateOf<Carrera?>(null) }
    val asignaturas by viewModel.asignaturas.collectAsState()

    LaunchedEffect(carreraId) {
        carrera = viewModel.getCarreraFake(carreraId)
        viewModel.getAsignaturasFake(carreraId)
    }
    carrera?.let {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(it.nombre) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { padding ->
            Column(Modifier.padding(padding).padding(16.dp)) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Descripción:", style = MaterialTheme.typography.labelMedium)
                        Text(it.descripcion, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(12.dp))

                        val completadas = asignaturas.count { it.completada }
                        val total = asignaturas.size
                        val porcentaje = if (total > 0) (completadas * 100 / total) else 0
                        val promedio = asignaturas.filter { it.completada }.map { it.nota }.average().takeIf { it.isFinite() } ?: 0.0

                        LinearProgressIndicator(progress = porcentaje / 100f, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(4.dp))
                        Text("Completado: $porcentaje%")
                        Text("Promedio: ${"%.2f".format(promedio)}")
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text("Asignaturas", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(asignaturas) { asignatura ->
                        Card(
                            modifier = Modifier.fillMaxWidth().clickable {
                                viewModel.setAginaturaFake(asignatura)
                                navController.navigate("detalle_asignatura/${carreraId}/${asignatura.id}")
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (asignatura.completada) CompleteGreen else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(asignatura.nombre, style = MaterialTheme.typography.titleMedium)
                                    Text("Nota: ${asignatura.nota}", style = MaterialTheme.typography.bodySmall)
                                }
                                Checkbox(
                                    checked = asignatura.completada,
                                    onCheckedChange = { viewModel.updateAsignaturaCompletionFake(asignatura.id, it) }
                                )
                            }
                        }
                    }
                }
            }
        }
    } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}