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

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("inicio") {
                        popUpTo("welcome") { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate("login")
                }
            )
        };

        // CORREGIDO: Se ha añadido la llamada a LoginScreen con sus parámetros
        composable("login") {
            LoginScreen(
                onLoginClick = {
                    // Acción al iniciar sesión: ir a "inicio" y limpiar la pila de navegación
                    navController.navigate("inicio") {
                        popUpTo("welcome") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    // Acción para ir al registro desde el login
                    navController.navigate("register")
                }
            )
        };

        // --- Rutas de la App Principal ---
        composable("inicio") {
            HomeScreen(navController = navController)
        };

        composable("animales") {
            AnimalListScreen(navController = navController)
        };

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
            CrearFincaScreen(
                // 1. La acción que ya tenías: navegar a "inicio" cuando la finca se cree.
                onFincaCreada = {
                    navController.popBackStack() // Es mejor usar popBackStack() para solo volver atrás
                },
                // 2. ✅ LA PARTE QUE FALTABA: La acción para el botón de "atrás".
                onBack = {
                    navController.popBackStack() // Simplemente vuelve a la pantalla anterior.
                }
            )
        }

        composable("miFinca") {
            MiFincaScreen( onNavigateBack = { navController.popBackStack()
            })
        }
        composable("miCuenta") {
            MiCuentaScreen( onNavigateBack = { navController.popBackStack()
            })
        }
    }
}
