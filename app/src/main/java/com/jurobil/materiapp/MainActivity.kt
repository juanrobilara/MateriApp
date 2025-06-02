package com.jurobil.materiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.jurobil.materiapp.ui.screens.AgregarCarreraScreen
import com.jurobil.materiapp.ui.screens.DetalleAsignaturaScreen
import com.jurobil.materiapp.ui.screens.DetalleCarreraScreen
import com.jurobil.materiapp.ui.screens.EditarCarreraScreen
import com.jurobil.materiapp.ui.screens.HomeScreen
import com.jurobil.materiapp.ui.screens.LoginScreen
import com.jurobil.materiapp.ui.screens.ProfileScreen
import com.jurobil.materiapp.ui.screens.ProfileScreenWrapper
import com.jurobil.materiapp.ui.screens.RegisterScreen
import com.jurobil.materiapp.ui.theme.MateriAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            MateriAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
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
            DetalleAsignaturaScreen(carreraId, asignaturaId)
        }
        composable("session") { SessionCheckScreen(navController = navController) }
        composable("profile") {
            ProfileScreenWrapper(navController)
        }
        }
    }



@Composable
fun SessionCheckScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            navController.navigate("home") {
                popUpTo("session") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("session") { inclusive = true }
            }
        }
    }

    // Puedes mostrar un loader opcionalmente
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}