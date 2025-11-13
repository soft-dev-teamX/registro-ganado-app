package com.softaprendizaje.ganado.ui.screens

// 1. IMPORTA TUS NUEVOS COMPONENTES
import com.softaprendizaje.ganado.ui.components.FincaBottomBar
import com.softaprendizaje.ganado.ui.components.FincaTopBar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

// El ViewModel sigue igual por ahora
class HomeViewModel : ViewModel() {
    var fincaNombre by mutableStateOf("La Rosa")

    // Tarjetas de inventario
    var totalMachos by mutableStateOf(2)
    var totalHembras by mutableStateOf<Int?>(null) // Lo ponemos null para que muestre "?"

    // Tarjetas de métricas
    var gananciaPeso by mutableStateOf(0.0)
    var totalCarne by mutableStateOf(2222.0)
    var totalLeche by mutableStateOf(0.0)
    var animalesProducidos by mutableStateOf(0)
}

// 🏠 Pantalla principal (ahora mucho más limpia)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = viewModel()) {

    Scaffold(
        topBar = {
            // 2. LLAMA A TU TOPBAR REUTILIZABLE
            FincaTopBar(
                onMenuClick = { /* TODO: Abrir el menú lateral (drawer) */ },
                onSearchClick = { /* TODO: Navegar a la pantalla de búsqueda */ }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("createAnimal") },
                // TODO: Cambia este color por el de tu tema
                containerColor = Color(0xFFE94444)
            ) {
                // TODO: Reemplazar 'Add' con tu icono personalizado de establo
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        },
        bottomBar = {
            // 3. LLAMA A TU BOTTOMBAR REUTILIZABLE
            FincaBottomBar(
                currentRoute = "inicio", // Le decimos que "inicio" es la seleccionada
                onNavigate = { nuevaRuta ->
                    // Aquí irá la lógica de navegación
                    // Por ahora solo imprimimos en la consola
                    println("Navegar a: $nuevaRuta")
                    // cuando tengas más pantallas, usarías:
                    // navController.navigate(nuevaRuta)
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tu contenido de Cards (el que ya tenías)
            Text(text = "Finca: ${viewModel.fincaNombre}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Machos: ${viewModel.totalMachos}")
                    Text("Hembras: ${viewModel.totalHembras}")
                    Text("Total carne: ${viewModel.totalCarne} kg")
                }
            }
            // TODO: Aquí irían las demás cards...
        }
    }
}
