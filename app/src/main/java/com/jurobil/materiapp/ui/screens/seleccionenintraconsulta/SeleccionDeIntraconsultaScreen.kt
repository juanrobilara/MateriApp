package com.jurobil.materiapp.ui.screens.seleccionenintraconsulta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jurobil.materiapp.ui.screens.homeScreen.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeleccionDeIntraconsultaScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    viewModel: IntraconsultaViewModel = hiltViewModel()
) {
    val carreras = viewModel.carreras


    val departamentoMap = mapOf(

        "1" to "Departamento de Ingeniería",
        "8" to "Departamento de Ingeniería",
        "11" to "Departamento de Ingeniería",
        "13" to "Departamento de Ingeniería",
        "16" to "Departamento de Ingeniería",
        "18" to "Departamento de Ingeniería",
        "19" to "Departamento de Ingeniería",
        "4" to "Departamento de Salud",
        "6" to "Departamento de Salud",
        "12" to "Departamento de Salud",
        "17" to "Departamento de Salud",
        "21" to "Departamento de Salud",
        "9" to "Departamento de Derecho y Ciencia Política",
        "2" to "Departamento de Económicas",
        "5" to "Departamento de Económicas",
        "20" to "Departamento de Económicas",
        "3" to "Departamento de Humanidades",
        "10" to "Departamento de Humanidades",
        "14" to "Departamento de Humanidades",
        "15" to "Departamento de Humanidades",
        "7" to "Departamento de Humanidades",
        "22" to "Departamento de Humanidades",

    )


    val carrerasPorDepartamento = remember(carreras) {
        carreras.groupBy { carrera ->
            departamentoMap[carrera.id] ?: "Escuela de Formación Continua"
        }
    }


    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Selección de intraconsulta", color = MaterialTheme.colorScheme.primary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            carrerasPorDepartamento.forEach { (departamento, carrerasDepartamento) ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            val current = expandedStates[departamento] ?: false
                            expandedStates[departamento] = !current
                        },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = departamento,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = if (expandedStates[departamento] == true) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (expandedStates[departamento] == true) "Colapsar" else "Expandir"
                            )
                        }
                    }
                }

                if (expandedStates[departamento] == true) {
                    items(carrerasDepartamento) { carrera ->
                        Card(
                            onClick = {
                                homeViewModel.saveCarrera(
                                    nombre = carrera.nombre,
                                    descripcion = carrera.descripcion,
                                    cantidadAsignaturas = carrera.cantidadAsignaturas
                                )
                                navController.popBackStack("home", false)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = carrera.nombre,
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}