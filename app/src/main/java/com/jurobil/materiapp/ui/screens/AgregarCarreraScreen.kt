package com.jurobil.materiapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jurobil.materiapp.domain.model.Asignatura
import com.jurobil.materiapp.ui.viewmodel.HomeViewModel

@Composable
fun AgregarCarreraScreen(navController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {

    var nombreCarrera by remember { mutableStateOf("") }
    var descripcionCarrera by remember { mutableStateOf("") }
    var cantidadAsignaturas by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = nombreCarrera,
            onValueChange = { nombreCarrera = it },
            label = { Text("Nombre de la carrera") }
        )

        OutlinedTextField(
            value = descripcionCarrera,
            onValueChange = { descripcionCarrera = it },
            label = { Text("Descripción") }
        )

        OutlinedTextField(
            value = cantidadAsignaturas,
            onValueChange = { newValue ->
                // Aceptar solo números
                if (newValue.all { it.isDigit() }) {
                    cantidadAsignaturas = newValue
                }
            },
            label = { Text("Cantidad de asignaturas") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            val cantidad = cantidadAsignaturas.toIntOrNull() ?: 0
            viewModel.saveCarrera(
                nombre = nombreCarrera,
                descripcion = descripcionCarrera,
                cantidadAsignaturas = cantidad
            )
            navController.popBackStack()
        }) {
            Text("Guardar carrera")
        }
    }
}