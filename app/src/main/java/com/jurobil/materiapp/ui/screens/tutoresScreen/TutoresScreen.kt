package com.jurobil.materiapp.ui.screens.tutoresScreen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.jurobil.materiapp.domain.model.Tutor
import com.jurobil.materiapp.ui.screens.homeScreen.MainScaffold
import com.jurobil.materiapp.ui.screens.homeScreen.viewmodel.HomeViewModel
import com.jurobil.materiapp.ui.screens.tutoresScreen.viewmodel.TutoresViewModel

@Composable
fun TutoresScreen(
    navController: NavHostController,
    viewModel: TutoresViewModel = hiltViewModel(),
    viewModelDos: HomeViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsState()
    val materiaFiltro by viewModel.materiaFiltro.collectAsState()
    val tutores by viewModel.tutoresFiltrados.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    val todasMaterias = listOf(
        "", "Matemática", "Física", "Química", "Programación I", "Estructuras de Datos",
        "Ingeniería de Software", "Comunicación Oral", "Historia del Arte", "Derecho Civil"
    )

    MainScaffold(
        title = "Tutorías",
        currentRoute = "tutores",
        onSignOut = {
            viewModelDos.signOut()
            navController.navigate("login") {
                popUpTo("tutores") { inclusive = true }
            }
        },
        onNavigate = { route ->
            navController.navigate(route) {
                popUpTo("tutores") { inclusive = false }
                launchSingleTop = true
            }
        },
        showFab = false
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.actualizarQuery(it) },
                label = { Text("Buscar tutor o área") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = materiaFiltro ?: "Filtrar por materia")
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    todasMaterias.forEach { materia ->
                        DropdownMenuItem(
                            text = { Text(if (materia.isEmpty()) "Todas" else materia) },
                            onClick = {
                                viewModel.actualizarFiltroMateria(if (materia.isEmpty()) null else materia)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(tutores) { tutor ->
                    TutorCard(tutor = tutor, onClick = {
                        //navegar a tutorCard
                    })
                }
            }
        }
    }
}

@Composable
fun TutorCard(tutor: Tutor, onClick: () -> Unit) {

    val fondoColor = lerp(
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.onSurface,
        0.1f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = fondoColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {

            if (tutor.fotoUrl != null) {
                AsyncImage(
                    model = tutor.fotoUrl,
                    contentDescription = "Foto de ${tutor.nombre}",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Tutor sin foto",
                    modifier = Modifier
                        .size(64.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                        .padding(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tutor.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = tutor.areaExpertise,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Materias: ${tutor.materias.joinToString()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}