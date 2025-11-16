package com.softaprendizaje.ganado.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.softaprendizaje.ganado.ui.components.FincaBottomBar
import com.softaprendizaje.ganado.ui.viewmodels.GanadoViewModel
import com.softaprendizaje.ganado.ui.data.Animal
import com.softaprendizaje.ganado.R
// ⬇️ IMPORTAMOS LA VARIABLE main_blue (asumimos que existe aquí)
import com.softaprendizaje.ganado.ui.theme.main_blue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalListScreen(navController: NavController) {

    val viewModel: GanadoViewModel = viewModel()

    // Estado para manejar la pestaña seleccionada: true=Machos, false=Hembras
    var showMales by remember { mutableStateOf(true) }

    // Lista de animales filtrada por el estado de 'showMales'
    val filteredAnimals = viewModel.animales.filter { it.esMacho == showMales }

    // Conteo para las pestañas
    val maleCount = viewModel.animales.count { it.esMacho }
    val femaleCount = viewModel.animales.size - maleCount


    Scaffold(
        topBar = {
            // 1. TopBar usando 'main_blue'
            CenterAlignedTopAppBar(
                title = { Text("Animales", color = Color.White) }, // Usamos Color.White directamente para el contraste
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = main_blue // ⬅️ Usando tu color personalizado
                )
            )
        },
        // 2. Eliminamos floatingActionButton (Botón con el '+')

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
                .fillMaxSize()
        ) {

            // 3. Eliminamos la Barra de Búsqueda (OutlinedTextField)

            // 4. Pestañas de Machos / Hembras
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GenderTab(
                    label = "Machos",
                    count = maleCount,
                    isSelected = showMales,
                    onClick = { showMales = true },
                    modifier = Modifier.weight(1f)
                )
                GenderTab(
                    label = "Hembras",
                    count = femaleCount,
                    isSelected = !showMales,
                    onClick = { showMales = false },
                    modifier = Modifier.weight(1f)
                )
            }

            // 5. Lista de Animales Filtrada
            if (filteredAnimals.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay ${if (showMales) "machos" else "hembras"} registrados aún.")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredAnimals) { animal ->
                        // Componente de Tarjeta
                        CompactAnimalCard(
                            animal = animal,
                            onAnimalClick = {
                                navController.navigate("editarAnimal/${animal.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Componentes Reutilizables
// -------------------------------------------------------------------------------------------------

@Composable
fun GenderTab(label: String, count: Int, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    // ⬇️ Usamos 'main_blue' para el color seleccionado
    val selectedColor = main_blue

    val containerColor = if (isSelected) selectedColor else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Contador (círculo)
            Text(
                text = count.toString(),
                color = if (isSelected) selectedColor else Color.White,
                modifier = Modifier
                    .clip(CircleShape)
                    // Usamos un color de fondo que contraste bien con el color del texto
                    .background(if (isSelected) Color.White else Color(0xFF5CB85C))
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(8.dp))
            // Etiqueta (Machos/Hembras)
            Text(
                text = label,
                color = contentColor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun CompactAnimalCard(
    animal: Animal,
    onAnimalClick: () -> Unit
)
{
    Card(
        onClick = onAnimalClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono del Animal
            Icon(
                painter = painterResource(id = R.drawable.ic_pets), // Usa tu icono real
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.4f))
                    .padding(8.dp)
            )

            Spacer(Modifier.width(16.dp))

            // Detalles del Animal
            Column(modifier = Modifier.weight(1f)) {
                // Fila: Propósito | Sexo - Raza
                Text(
                    text = "${animal.proposito} | ${if (animal.esMacho) "Macho" else "Hembra"} - ${animal.raza}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                // Fila: Raza | Nº Número
                Text(
                    text = "${animal.raza} | Nº ${animal.numero}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                )
            }

            // Número del animal (el número grande a la derecha)
            Text(
                text = animal.numero,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = main_blue // ⬅️ Usando tu color personalizado
            )
        }
    }
}