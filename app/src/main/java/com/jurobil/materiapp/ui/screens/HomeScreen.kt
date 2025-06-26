package com.jurobil.materiapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jurobil.materiapp.R
import com.jurobil.materiapp.domain.model.Carrera
import com.jurobil.materiapp.domain.model.CarreraResumen
import com.jurobil.materiapp.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val greeting by viewModel.greeting.collectAsState()
    val carreras by viewModel.carreras.collectAsState()
    val resumenCarreras by viewModel.resumenCarreras.collectAsState()

    MainScaffold(
        title = "Bienvenido, $greeting",
        currentRoute = "home",
        onSignOut = {
            viewModel.signOut()
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        },
        onNavigate = { route ->
            navController.navigate(route) {
                popUpTo("home") { inclusive = false }
                launchSingleTop = true
            }
        },
        showFab = true
    ) { innerPadding ->
        HomeScreenContent(
            greeting = greeting,
            carreras = carreras,
            resumenes = resumenCarreras,
            onDeleteCarrera = { id -> viewModel.deleteCarrera(id) },
            onEditCarrera = { id -> navController.navigate("editar_carrera/$id") },
            onNavigate = { route ->
                navController.navigate(route) {
                    popUpTo("home") { inclusive = false }
                    launchSingleTop = true
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreenContent(
    greeting: String,
    carreras: List<Carrera>,
    resumenes: Map<String, CarreraResumen>,
    onDeleteCarrera: (String) -> Unit,
    onEditCarrera: (String) -> Unit,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedCarreraId by remember { mutableStateOf<String?>(null) }

    if (showDialog && selectedCarreraId != null) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                selectedCarreraId = null
            },
            title = { Text("Acciones sobre carrera") },
            text = { Text("¿Qué querés hacer con esta carrera?") },
            confirmButton = {
                TextButton(onClick = {
                    onEditCarrera(selectedCarreraId!!)
                    showDialog = false
                    selectedCarreraId = null
                }) {
                    Text("Editar")
                }
            },
            dismissButton = {
                Column {
                    TextButton(onClick = {
                        onDeleteCarrera(selectedCarreraId!!)
                        showDialog = false
                        selectedCarreraId = null
                    }) {
                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                    TextButton(onClick = {
                        showDialog = false
                        selectedCarreraId = null
                    }) {
                        Text("Cancelar")
                    }
                }
            }
        )
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(carreras) { carrera ->
                val resumen = resumenes[carrera.id]
                val porcentaje = resumen?.porcentaje ?: 0
                val fondoColor = colorPorPorcentaje(porcentaje)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .combinedClickable(
                            onClick = {
                                onNavigate("detalle_carrera/${carrera.id}")
                            },
                            onLongClick = {
                                selectedCarreraId = carrera.id
                                showDialog = true
                            }
                        ),
                    colors = CardDefaults.cardColors(containerColor = fondoColor),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(carrera.nombre, style = MaterialTheme.typography.titleLarge, color = Color.Black)
                        Text(carrera.descripcion, style = MaterialTheme.typography.bodySmall, color = Color.Black)
                        resumen?.let {
                            Spacer(Modifier.height(4.dp))
                            Text("Completado: ${it.porcentaje}%", color = Color.Black)
                            Text("Promedio: ${"%.2f".format(it.promedio)}", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    title: String,
    currentRoute: String,
    onSignOut: () -> Unit,
    onNavigate: (String) -> Unit,
    showFab: Boolean = true,
    content: @Composable (PaddingValues) -> Unit
) {
    val items = listOf(
        BottomNavItem("home", Icons.Default.Home, "Inicio"),
        BottomNavItem("materias_curso", Icons.Default.School, "En curso"),
        BottomNavItem("tramites", Icons.AutoMirrored.Filled.Assignment, "Trámites"),
        BottomNavItem("configuracion", Icons.Default.Settings, "Configuración")
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(title)
                        Spacer(Modifier.width(8.dp))
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigate("profile") }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                    IconButton(onClick = onSignOut) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = { onNavigate(item.route) },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        },
        floatingActionButton = {
            if (showFab) {
                FloatingActionButton(onClick = { onNavigate("agregar_carrera") }) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar carrera")
                }
            }
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}


@Composable
fun colorPorPorcentaje(porcentaje: Int): Color {
    return when (porcentaje) {
        in 0..29 -> Color.White
        in 30..59 -> Color(0xFFFFF9C4) // Amarillo pastel
        in 60..98 -> Color(0xFFFFE0B2) // Naranja pastel
        in 99..100 -> Color(0xFFC8E6C9) // Verde pastel
        else -> Color.Transparent
    }
}

data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)


@Preview(showBackground = true)
@Composable
fun HomeScreenContentPreview() {
    MaterialTheme {
        HomeScreenContent(
            greeting = "¡Hola, usuario!",
            onNavigate = {},
            carreras = emptyList(),
            onDeleteCarrera = {},
            onEditCarrera = {},
            resumenes = TODO(),
        )
    }
}