package com.softaprendizaje.ganado.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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

    // Estado: Machos o Hembras
    var showMales by remember { mutableStateOf(true) }

    // Estado: búsqueda
    var searchQuery by remember { mutableStateOf("") }

    // Estado: filtro de producidos
    var soloProducidos by remember { mutableStateOf(false) }

    // Conteos
    val maleCount = viewModel.animales.count { it.esMacho }
    val femaleCount = viewModel.animales.size - maleCount

    // ------ FILTRADO COMBINADO ------
    val filteredAnimals = viewModel.animales
        // 1. Machos / Hembras
        .filter { it.esMacho == showMales }
        // 2. Búsqueda
        .filter {
            searchQuery.isBlank() ||
                    it.numero.contains(searchQuery, ignoreCase = true) ||
                    it.raza.contains(searchQuery, ignoreCase = true) ||
                    it.proposito.contains(searchQuery, ignoreCase = true)
        }
        // 3. Filtro "Producidos"
        .filter {
            if (soloProducidos) it.producido else true
        }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Animales", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = main_blue
                )
            )
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
                .fillMaxSize()
        ) {

            // ----------------- TABS MACHOS / HEMBRAS -----------------
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

            // ----------------- BARRA DE BÚSQUEDA -----------------
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = { Text("Buscar por número, raza o propósito") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                singleLine = true
            )

            // ----------------- FILTROS (CHIPS) -----------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = soloProducidos,
                    onClick = { soloProducidos = !soloProducidos },
                    label = { Text("Producidos") }
                )
            }

            // ----------------- LISTA -----------------
            if (filteredAnimals.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay animales que coincidan con la búsqueda o filtros.")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredAnimals) { animal ->
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