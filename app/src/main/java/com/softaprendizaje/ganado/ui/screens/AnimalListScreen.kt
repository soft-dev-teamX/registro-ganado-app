package com.softaprendizaje.ganado.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.softaprendizaje.ganado.ui.components.FincaBottomBar
import com.softaprendizaje.ganado.ui.viewmodels.GanadoViewModel
import com.softaprendizaje.ganado.ui.data.Animal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalListScreen(navController: NavController) {

    // El ViewModel ya escucha Firebase en tiempo real por su cuenta
    val viewModel: GanadoViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Animales") })
        },
        bottomBar = {
            FincaBottomBar(
                currentRoute = "animales",
                onNavigate = { ruta ->
                    if (ruta != "animales") {
                        navController.navigate(ruta) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {

            Text(
                text = "Total: ${viewModel.animales.size} animales 🐄",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp)
            )

            if (viewModel.animales.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay animales registrados aún.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(viewModel.animales) { animal ->
                        AnimalCard(animal)
                    }
                }
            }
        }
    }
}

@Composable
fun AnimalCard(animal: Animal) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🐄 Nº ${animal.numero}", style = MaterialTheme.typography.titleMedium)
            Text("Raza: ${animal.raza}")
            Text("Propósito: ${animal.proposito}")
            Text("Nacimiento: ${animal.fechaNacimiento}")
            Text(if (animal.esMacho) "Sexo: Macho" else "Sexo: Hembra")
        }
    }
}
