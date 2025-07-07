package com.jurobil.materiapp.ui.screens.ajustesAyudaScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
                    text = "Centro de Ayuda",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                AyudaCategoria(
                    titulo = "Gestión de Carreras",
                    preguntas = listOf(
                        "¿Cómo agrego una carrera?" to "Desde la pantalla principal, presioná el botón de agregar y completá los datos de la carrera.",
                        "¿Cómo edito una carrera?" to "Desde la pantalla principal, tocá sobre la carrera y seleccioná la opción de editar.",
                        "¿Cómo elimino una carrera?" to "En la pantalla de detalle de la carrera, encontrarás la opción para eliminarla.",
                        "¿Puedo tener varias carreras al mismo tiempo?" to "Sí, la aplicación permite que gestiones múltiples carreras de manera independiente."
                    )
                )
            }

            item {
                AyudaCategoria(
                    titulo = "Gestión de Materias",
                    preguntas = listOf(
                        "¿Cómo agrego una materia?" to "Las materias se agregan al momento de crear la carrera o desde el detalle de la misma.",
                        "¿Cómo marco una materia como finalizada?" to "Dentro del detalle de la materia, podés marcarla como finalizada y cargar la nota.",
                        "¿Cómo veo el progreso de mi carrera?" to "En la lista principal de carreras, se muestra el porcentaje de materias completadas.",
                        "¿Puedo editar el nombre o los datos de una materia?" to "Sí, ingresando al detalle de la materia podés modificar su información."
                    )
                )
            }

            item {
                AyudaCategoria(
                    titulo = "Apariencia y Configuración",
                    preguntas = listOf(
                        "¿Cómo activo el modo oscuro?" to "En Configuración > Apariencia, podés activar o desactivar el modo oscuro.",
                        "¿Puedo personalizar los colores de la aplicación?" to "Actualmente no es posible, pero estamos trabajando en opciones de personalización.",
                        "¿Cómo actualizo mi información de perfil?" to "Desde la sección de Perfil, podés modificar tu nombre y foto de perfil.",
                        "¿Cómo cierro sesión?" to "En cualquier pantalla, tocá el menú de usuario y seleccioná 'Cerrar sesión'."
                    )
                )
            }

            item {
                AyudaCategoria(
                    titulo = "Soporte",
                    preguntas = listOf(
                        "¿Dónde puedo solicitar soporte?" to "Podés escribirnos a grupointerfaces@gmail.com para recibir ayuda.",
                        "¿Hay una comunidad de usuarios?" to "Próximamente estaremos habilitando un espacio de comunidad para compartir experiencias.",
                        "¿Puedo sugerir mejoras?" to "Sí, nos encanta recibir sugerencias. Envialas a grupointerfaces@gmail.com.",
                        "¿La aplicación es gratuita?" to "Actualmente, la app es gratuita. En el futuro podríamos ofrecer funciones premium."
                    )
                )
            }
        }
    }
}


@Composable
fun AyudaCategoria(titulo: String, preguntas: List<Pair<String, String>>) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Cerrar" else "Abrir"
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(Modifier.padding(top = 12.dp)) {
                    preguntas.forEach { (question, answer) ->
                        HelpItem(question = question, answer = answer)
                        Spacer(Modifier.height(8.dp))
                    }
                }
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
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
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