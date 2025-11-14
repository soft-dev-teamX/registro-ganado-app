package com.softaprendizaje.ganado

// Importaciones necesarias para las nuevas rutas
import com.softaprendizaje.ganado.ui.screens.AnimalListScreen
import com.softaprendizaje.ganado.ui.screens.CreateAnimalScreen
import com.softaprendizaje.ganado.ui.screens.HomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.softaprendizaje.ganado.ui.screens.CrearFincaScreen
import com.softaprendizaje.ganado.ui.screens.LoginScreen
import com.softaprendizaje.ganado.ui.screens.MiCuentaScreen
import com.softaprendizaje.ganado.ui.screens.MiFincaScreen
import com.softaprendizaje.ganado.ui.screens.RegisterScreen
import com.softaprendizaje.ganado.ui.screens.WelcomeScreen
import com.softaprendizaje.ganado.ui.theme.RegistroGanadoTheme

// La Activity principal se mantiene sencilla
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegistroGanadoTheme {
                AppNavigation() // El único trabajo de la Activity es lanzar la navegación
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "welcome") {
        // --- Rutas de autenticación ---
        composable("welcome") {
            WelcomeScreen(
                onLoginClick = { navController.navigate("login") },
                onRegisterClick = { navController.navigate("register") }
            )
        };

        // CORREGIDO
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("inicio") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        };

        composable("login") {
            LoginScreen(
                onLoginClick = {
                    navController.navigate("inicio") {
                        popUpTo("welcome") { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate("register") }
            )
        };

        // --- Rutas de la App Principal ---
        composable("inicio") {
            HomeScreen(navController = navController)
        };

        composable("animales") {
            AnimalListScreen(navController = navController)
        };

        // CORREGIDO
        composable("crearAnimal") {
            CreateAnimalScreen(
                onAnimalCreated = {
                    navController.popBackStack()
                }
            )
        };

        // --- Rutas restantes ---
        composable("produccion") { /* Placeholder */ };
        composable("alertas") { /* Placeholder */ }

        composable("crearFinca") {
            CrearFincaScreen(onFincaCreada = { navController.navigate("inicio") })
        }

        composable("miFinca") { MiFincaScreen() }
        composable("miCuenta") { MiCuentaScreen() }

    }
}
