package com.jurobil.materiapp.ui.screens.ajustesAyudaScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jurobil.materiapp.ui.screens.homeScreen.MainScaffold

@Composable
fun AjustesAyudaScreen(navController: NavHostController) {
    MainScaffold(
        title = "Ayuda",
        currentRoute = "ajustes_ayuda",
        onNavigate = { navController.navigate(it) },
        onSignOut = { navController.navigate("login") },
        showFab = false
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Preguntas Frecuentes",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                HelpItem(
                    question = "¿Cómo agrego una carrera?",
                    answer = "Desde la pantalla principal, presioná el botón de agregar y completá los datos de la carrera."
                )
            }

            item {
                HelpItem(
                    question = "¿Cómo puedo modificar o eliminar una materia?",
                    answer = "Ingresá al detalle de la carrera y seleccioná la materia que deseas editar o eliminar."
                )
            }

            item {
                HelpItem(
                    question = "¿Cómo activo el modo oscuro?",
                    answer = "En Configuración > Apariencia, podés activar o desactivar el modo oscuro."
                )
            }

            item {
                HelpItem(
                    question = "¿Dónde puedo solicitar soporte?",
                    answer = "Podés escribirnos a grupointerfaces@gmail.com para recibir ayuda."
                )
            }
        }
    }
}


@Composable
fun HelpItem(question: String, answer: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
            .padding(16.dp)
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = answer,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}