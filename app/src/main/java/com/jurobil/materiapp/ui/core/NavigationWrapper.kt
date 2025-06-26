package com.jurobil.materiapp.ui.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jurobil.materiapp.ui.screens.agregarCarreraScreen.AgregarCarreraScreen
import com.jurobil.materiapp.ui.screens.configuracionScreen.ConfiguracionScreen
import com.jurobil.materiapp.ui.screens.detalleAsignaturaScreen.DetalleAsignaturaScreen
import com.jurobil.materiapp.ui.screens.detalleCarreraScreen.DetalleCarreraScreen
import com.jurobil.materiapp.ui.screens.editarCarreraScreen.EditarCarreraScreen
import com.jurobil.materiapp.ui.screens.homeScreen.HomeScreen
import com.jurobil.materiapp.ui.screens.loginScreen.LoginScreen
import com.jurobil.materiapp.ui.screens.materiasEnCursoScreen.MateriasEnCursoScreen
import com.jurobil.materiapp.ui.screens.profileScreenWrapper.ProfileScreenWrapper
import com.jurobil.materiapp.ui.screens.registerScreen.RegisterScreen
import com.jurobil.materiapp.ui.screens.tramitesScreen.TramitesScreen
import com.jurobil.materiapp.ui.screens.sessionCheckScreen.SessionCheckScreen

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "session") {
        composable("login") { LoginScreen(navController = navController) }
        composable("register") { RegisterScreen(navController = navController) }
        composable("home") { HomeScreen(navController = navController) }
        composable("agregar_carrera") {AgregarCarreraScreen(navController = navController)}
        composable("editar_carrera/{carreraId}") { backStackEntry ->
            val carreraId = backStackEntry.arguments?.getString("carreraId") ?: ""
            EditarCarreraScreen(navController, carreraId)
        }
        composable(
            route = "detalle_carrera/{carreraId}",
            arguments = listOf(navArgument("carreraId") { type = NavType.StringType })
        ) { backStackEntry ->
            val carreraId = backStackEntry.arguments?.getString("carreraId") ?: ""
            DetalleCarreraScreen(carreraId = carreraId, navController = navController)
        }
        composable(
            route = "detalle_asignatura/{carreraId}/{asignaturaId}",
            arguments = listOf(
                navArgument("carreraId") { type = NavType.StringType },
                navArgument("asignaturaId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val carreraId = backStackEntry.arguments?.getString("carreraId") ?: ""
            val asignaturaId = backStackEntry.arguments?.getString("asignaturaId") ?: ""
            DetalleAsignaturaScreen(carreraId, asignaturaId, navHostController = navController)
        }
        composable("session") { SessionCheckScreen(navController = navController) }
        composable("profile") {
            ProfileScreenWrapper(navController)
        }
        composable("materias_curso") {
            MateriasEnCursoScreen(navController = navController)
        }
        composable("tramites") {
            TramitesScreen(navController = navController)
        }
        composable("configuracion") {
            ConfiguracionScreen(navController = navController)
        }
    }
}