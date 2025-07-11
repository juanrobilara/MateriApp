package com.jurobil.materiapp.ui.screens.homeScreen.tabs.tramitesScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.jurobil.materiapp.ui.screens.homeScreen.MainScaffold
import com.jurobil.materiapp.ui.screens.homeScreen.tabs.tramitesScreen.model.OptionsMenu
import com.jurobil.materiapp.ui.screens.homeScreen.tabs.tramitesScreen.model.TramiteItem
import com.jurobil.materiapp.ui.screens.homeScreen.tabs.tramitesScreen.viewmodel.TramitesViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TramitesScreen(
    navController: NavHostController,
    viewModel: TramitesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    MainScaffold(
        title = "Tus Trámites",
        currentRoute = "tramites",
        onSignOut = {
            navController.navigate("login") {
                popUpTo("tramites") { inclusive = true }
            }
        },
        onNavigate = { route ->
            navController.navigate(route) {
                popUpTo("tramites") { inclusive = false }
                launchSingleTop = true
            }
        },
        showFab = false
    ) { innerPadding ->

        when (uiState.optionsMenu) {
            OptionsMenu.MAIN_CONTENT_SCREEN -> {
                TramitesGrid(
                    padding = innerPadding,
                    onTramiteClick = { option ->
                        viewModel.changeOptionsMenu(option)
                    }
                )
            }

            else -> {
                TramiteContent(
                    option = uiState.optionsMenu,
                    onBack = { viewModel.changeOptionsMenu(OptionsMenu.MAIN_CONTENT_SCREEN) },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun TramitesGrid(
    padding: PaddingValues,
    onTramiteClick: (OptionsMenu) -> Unit
) {
    val tramites = listOf(
        TramiteItem("Calendario Académico", Icons.Default.Event, OptionsMenu.CALENDARIO_SCREEN),
        TramiteItem("Inscripción a Materias", Icons.Default.Edit, OptionsMenu.INSCRIPCION_SCREEN),
        TramiteItem("Inscripción a Finales", Icons.Default.BookOnline, OptionsMenu.INSCRIPCION_SCREEN),
        TramiteItem("Solicitud de Analítico Parcial", Icons.Default.Assignment, OptionsMenu.ANALITICO_SCREEN),
        TramiteItem("Verificación", Icons.Default.Verified, OptionsMenu.VERIFICACION_SCREEN),
        TramiteItem("Reclamo de Notas de Examen", Icons.Default.ReportProblem, OptionsMenu.RECLAMO_SCREEN),
        TramiteItem("Planes de Estudio y Carreras", Icons.Default.MenuBook, OptionsMenu.PLANES_SCREEN),
        TramiteItem("Cambio y Simultaneidad", Icons.Default.SwapHoriz, OptionsMenu.CAMBIO_SCREEN),
        TramiteItem("Constancia de Alumno Regular", Icons.Default.Description, OptionsMenu.CONSTANCIA_SCREEN),
        TramiteItem("Boleto Estudiantil", Icons.Default.DirectionsBus, OptionsMenu.BOLETO_SCREEN),
        TramiteItem("Solicitud de Becas", Icons.Default.Money, OptionsMenu.BOLETO_SCREEN),
        TramiteItem("Certificado de Examen Libre", Icons.Default.Quiz, OptionsMenu.CERTIFICADO_SCREEN),
        TramiteItem("Certificado de Finalización", Icons.Default.School, OptionsMenu.FINALIZACION_SCREEN),
        TramiteItem("Más acciones", Icons.Default.MoreHoriz, OptionsMenu.MAS_ACCIONES_SCREEN)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = padding.calculateTopPadding() + 16.dp,
            bottom = padding.calculateBottomPadding() + 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(tramites) { tramite ->
            TramiteButton(
                tramite = tramite,
                onClick = { onTramiteClick(tramite.option) }
            )
        }
    }


}

@Composable
fun TramiteContent(
    option: OptionsMenu,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onBack) {
            Text("Volver")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (option) {
            OptionsMenu.CONSTANCIA_SCREEN -> Text("Contenido de Constancia")
            OptionsMenu.INSCRIPCION_SCREEN -> Text("Contenido de Inscripción")
            OptionsMenu.VERIFICACION_SCREEN -> Text("Contenido de Verificación")
            OptionsMenu.CERTIFICADO_SCREEN -> Text("Contenido de Certificado de Examen Libre")
            OptionsMenu.ANALITICO_SCREEN -> Text("Contenido de Analítico Parcial")
            OptionsMenu.BOLETO_SCREEN -> Text("Contenido de Boleto estudiantil")
            OptionsMenu.CALENDARIO_SCREEN -> Text("Contenido de Calendario Académico")
            OptionsMenu.RECLAMO_SCREEN -> Text("Contenido de Reclamo de Notas")
            OptionsMenu.PLANES_SCREEN -> Text("Contenido de Planes de Estudio")
            OptionsMenu.CAMBIO_SCREEN -> Text("Contenido de Cambio y Simultaneidad de Carrera")
            OptionsMenu.FINALIZACION_SCREEN -> Text("Contenido de Certificado de Finalización")
            OptionsMenu.MAS_ACCIONES_SCREEN -> Text("Contenido de Más acciones")
            else -> Text("Opción no reconocida.")
        }
    }
}

@Composable
fun TramiteButton(
    tramite: TramiteItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .clickable { onClick() }
            .padding(16.dp)
            .fillMaxWidth()
            .height(90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = tramite.icono,
            contentDescription = tramite.nombre,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = tramite.nombre,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}


