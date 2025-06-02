package com.jurobil.materiapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jurobil.materiapp.domain.model.Asignatura
import com.jurobil.materiapp.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarCarreraScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    var nombreCarrera by remember { mutableStateOf("") }
    var descripcionCarrera by remember { mutableStateOf("") }
    var cantidadAsignaturas by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Agregar carrera") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nombreCarrera,
                onValueChange = { nombreCarrera = it },
                label = { Text("Nombre de la carrera") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcionCarrera,
                onValueChange = { descripcionCarrera = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = cantidadAsignaturas,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        cantidadAsignaturas = newValue
                    }
                },
                label = { Text("Cantidad de asignaturas") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val cantidad = cantidadAsignaturas.toIntOrNull() ?: 0
                    viewModel.saveCarrera(
                        nombre = nombreCarrera,
                        descripcion = descripcionCarrera,
                        cantidadAsignaturas = cantidad
                    )
                    navController.popBackStack()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Guardar carrera")
            }
        }
    }
}


