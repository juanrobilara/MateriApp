package com.jurobil.materiapp.ui.screens.detalleAsignaturaScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jurobil.materiapp.domain.fakeRepository.FakeRepository
import com.jurobil.materiapp.domain.model.Asignatura
import com.jurobil.materiapp.ui.screens.homeScreen.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleAsignaturaScreen(
    carreraId: String,
    asignaturaId: String,
    navHostController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val currentAsignatura by viewModel.currentAsignatura.collectAsState()
    var asignatura by remember { mutableStateOf(currentAsignatura) }
    var nombre by remember { mutableStateOf("") }
    var nota by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }


    Log.i("Klyxdev", "setAginaturaFake: $currentAsignatura")

    // Cargar datos de la asignatura
    LaunchedEffect(asignaturaId) {
        viewModel.getAsignaturasFake(asignaturaId)
    }

    if (currentAsignatura == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Editar Asignatura") },
                    navigationIcon = {
                        IconButton(onClick = { navHostController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Detalles actuales:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Nombre: ${currentAsignatura!!.nombre}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre de la Asignatura") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = nota,
                    onValueChange = { nota = it },
                    label = { Text("Nota") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = observaciones,
                    onValueChange = { observaciones = it },
                    label = { Text("Observaciones") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@Button
                        val notaDouble = nota.toDoubleOrNull()
                        isSaving = true

                        FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(uid)
                            .collection("carreras")
                            .document(carreraId)
                            .collection("asignaturas")
                            .document(asignaturaId)
                            .update(
                                mapOf(
                                    "nombre" to nombre,
                                    "nota" to notaDouble,
                                    "observaciones" to observaciones
                                )
                            )
                            .addOnSuccessListener {
                                isSaving = false
                                navHostController.popBackStack()
                            }
                            .addOnFailureListener {
                                isSaving = false
                            }
                    },
                    enabled = !isSaving,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Guardar cambios")
                    }
                }
            }
        }
    }
}