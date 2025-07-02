package com.jurobil.materiapp.ui.screens.homeScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.CoPresent
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jurobil.materiapp.domain.model.Carrera
import com.jurobil.materiapp.domain.model.CarreraResumen
import com.jurobil.materiapp.ui.core.theme.CompleteGreen
import com.jurobil.materiapp.ui.core.theme.ProgressOrange
import com.jurobil.materiapp.ui.core.theme.WarningYellow
import com.jurobil.materiapp.ui.screens.homeScreen.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val greeting by viewModel.greeting.collectAsState()
    val carreras by viewModel.carreras.collectAsState()
    val resumenCarreras by viewModel.resumenCarreras.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var selectedCarreraId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) { viewModel.reloadCarreras() }

    MainScaffold(
        title = "Bienvenido, $greeting",
        currentRoute = "home",
        onSignOut = {
            viewModel.signOut()
            navController.navigate("login") { popUpTo("home") { inclusive = true } }
        },
        onNavigate = { route ->
            navController.navigate(route) { popUpTo("home") { inclusive = false }; launchSingleTop = true }
        },
        showFab = true
    ) { innerPadding ->

        HomeScreenContent(
            carreras = carreras,
            resumenes = resumenCarreras,
            onNavigate = { route -> navController.navigate(route) },
            onLongClickCarrera = {
                selectedCarreraId = it
                showDialog = true
            },
            modifier = Modifier.padding(innerPadding)
        )

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
                        navController.navigate("editar_carrera/${selectedCarreraId}")
                        showDialog = false
                        selectedCarreraId = null
                    }) {
                        Text("Editar")
                    }
                },
                dismissButton = {
                    Column {
                        TextButton(onClick = {
                            viewModel.deleteCarreraMock(selectedCarreraId!!)
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
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CarreraCard(
    carrera: Carrera,
    resumen: CarreraResumen?,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val porcentaje = resumen?.porcentaje ?: 0
    val fondoColor = when (porcentaje) {
        in 0..29 -> lerp(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.onSurface,
            0.1f
        )
        in 30..59 -> WarningYellow
        in 60..98 -> ProgressOrange
        else -> CompleteGreen
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        colors = CardDefaults.cardColors(containerColor = fondoColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(carrera.nombre, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(4.dp))
            Text(carrera.descripcion.ifBlank { "Sin descripción" }, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Asignaturas: ${carrera.cantidadAsignaturas}")
                resumen?.let {
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Completado: ${it.porcentaje}%")
                        Text("Promedio: ${"%.2f".format(it.promedio)}")
                    }
                }
            }
        }
    }
}


@Composable
fun HomeScreenContent(
    carreras: List<Carrera>,
    resumenes: Map<String, CarreraResumen>,
    onNavigate: (String) -> Unit,
    onLongClickCarrera: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(carreras) { carrera ->
            CarreraCard(
                carrera = carrera,
                resumen = resumenes[carrera.id],
                onClick = { onNavigate("detalle_carrera/${carrera.id}") },
                onLongClick = { onLongClickCarrera(carrera.id) }
            )
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
        BottomNavItem("tutores", Icons.Default.CoPresent, "Tutorías"),
        BottomNavItem("tramites", Icons.AutoMirrored.Filled.Assignment, "Trámites"),
        BottomNavItem("configuracion", Icons.Default.Settings, "Ajustes")
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
                FloatingActionButton(onClick = { onNavigate("seleccionar_fuente_de_carrera") }) {
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


