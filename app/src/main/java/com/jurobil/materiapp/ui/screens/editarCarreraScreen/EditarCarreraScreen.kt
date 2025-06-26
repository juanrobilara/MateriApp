package com.jurobil.materiapp.ui.screens.editarCarreraScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavHostController
import com.jurobil.materiapp.ui.screens.homeScreen.viewmodel.HomeViewModel

@Composable
fun EditarCarreraScreen(
    navController: NavHostController,
    carreraId: String,
    viewModel: HomeViewModel = hiltViewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getCarrera(carreraId) { carrera ->
            nombre = carrera?.nombre ?: ""
            descripcion = carrera?.descripcion ?: ""
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre de la carrera") }
        )
        TextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.updateCarrera(carreraId, nombre, descripcion)
            navController.popBackStack()
        }) {
            Text("Guardar cambios")
        }
    }

}