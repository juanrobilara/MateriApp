package com.jurobil.materiapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jurobil.materiapp.domain.model.Asignatura
import com.jurobil.materiapp.ui.viewmodel.HomeViewModel

@Composable
fun DetalleAsignaturaScreen(
    carreraId: String,
    asignaturaId: String,
    navHostController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    var asignatura by remember { mutableStateOf<Asignatura?>(null) }
    var nombre by remember { mutableStateOf("") }
    var nota by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    // Cargar datos de la asignatura
    LaunchedEffect(asignaturaId) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@LaunchedEffect
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("carreras")
            .document(carreraId)
            .collection("asignaturas")
            .document(asignaturaId)
            .get()
            .addOnSuccessListener { doc ->
                val asign = doc.toObject(Asignatura::class.java)
                asign?.let {
                    asignatura = it
                    nombre = it.nombre // <- esto faltaba
                    nota = it.nota?.toString() ?: ""
                    observaciones = it.observaciones ?: ""
                }
            }
    }

    if (asignatura == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Nombre original: ${asignatura!!.nombre}", style = MaterialTheme.typography.titleMedium)

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
                modifier = Modifier.fillMaxWidth()
            )

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
                            navHostController.popBackStack() // <- aquí sí, luego de guardar
                        }
                        .addOnFailureListener {
                            isSaving = false
                        }
                },
                enabled = !isSaving,
                modifier = Modifier.align(Alignment.End)
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