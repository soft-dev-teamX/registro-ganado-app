package com.softaprendizaje.ganado

import com.softaprendizaje.ganado.ui.screens.AnimalListScreen
import com.softaprendizaje.ganado.ui.screens.CreateAnimalScreen
import com.softaprendizaje.ganado.ui.screens.HomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.softaprendizaje.ganado.ui.screens.CrearFincaScreen
import com.softaprendizaje.ganado.ui.screens.EditAnimalScreen
import com.softaprendizaje.ganado.ui.screens.EditFincaScreen
import com.softaprendizaje.ganado.ui.screens.EditProfileScreen
import com.softaprendizaje.ganado.ui.screens.LoginScreen
import com.softaprendizaje.ganado.ui.screens.MiCuentaScreen
import com.softaprendizaje.ganado.ui.screens.MiFincaScreen
import com.softaprendizaje.ganado.ui.screens.RegisterScreen
import com.softaprendizaje.ganado.ui.screens.WelcomeScreen
import com.softaprendizaje.ganado.ui.theme.RegistroGanadoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegistroGanadoTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(
                onLoginClick = { navController.navigate("login") },
                onRegisterClick = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("inicio") {
                        popUpTo("welcome") { inclusive = true }
                    }
                },
                onLoginClick = { navController.navigate("login") }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginClick = {
                    navController.navigate("inicio") {
                        popUpTo("welcome") { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate("register") }
            )
        }

        composable("inicio") {
            HomeScreen(navController = navController)
        }

        composable("animales") {
            AnimalListScreen(navController = navController)
        }

        composable("crearAnimal") {
            CreateAnimalScreen(
                onAnimalCreated = { navController.popBackStack() }
            )
        }

        composable("produccion") { }
        composable("alertas") { }

        composable("crearFinca") {
            CrearFincaScreen(
                onFincaCreada = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable("miFinca") {
            MiFincaScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEditFinca = { navController.navigate("editarFinca") }
            )
        }

        composable("editarFinca") {
            EditFincaScreen(onSaveSuccess = { navController.popBackStack() })
        }

        composable("miCuenta") {
            MiCuentaScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEditProfile = { navController.navigate("editarPerfil") }
            )
        }

        composable("editarPerfil") {
            EditProfileScreen(onSaveSuccess = { navController.popBackStack() })
        }

        composable(
            route = "editarAnimal/{animalId}",
            arguments = listOf(navArgument("animalId") { type = NavType.StringType })
        ) { backStackEntry ->

            val animalId = backStackEntry.arguments?.getString("animalId")

            if (animalId != null) {
                EditAnimalScreen(navController = navController, animalId = animalId)
            } else {
                Text("Error: ID no proporcionado")
            }
        }
    }
}
