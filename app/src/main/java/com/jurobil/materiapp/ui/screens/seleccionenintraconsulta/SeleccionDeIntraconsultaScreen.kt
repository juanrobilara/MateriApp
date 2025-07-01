package com.jurobil.materiapp.ui.screens.seleccionenintraconsulta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Selección de intraconsulta") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
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
            items(carreras) { carrera ->
                Card(
                    onClick = {
                        homeViewModel.saveCarrera(
                            nombre = carrera.nombre,
                            descripcion = carrera.descripcion,
                            cantidadAsignaturas = carrera.cantidadAsignaturas
                        )
                        navController.popBackStack("home", false)
                        // TODO Arreglar y agregar a la lista de carreras
                    },
                    modifier = Modifier
                        //.padding(16.dp)
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
            }
        }
    }
}