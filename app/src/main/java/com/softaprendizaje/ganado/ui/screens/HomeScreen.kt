package com.softaprendizaje.ganado.ui.screens

// Componentes propios
import com.softaprendizaje.ganado.ui.components.FincaBottomBar
import com.softaprendizaje.ganado.ui.components.FincaTopBar
import com.softaprendizaje.ganado.ui.components.MetricCard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.softaprendizaje.ganado.ui.viewmodels.GanadoViewModel
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    navController: NavController
) {
    val viewModel: GanadoViewModel = viewModel()

    // ➤ Drawer y Scope:
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // ➤ Usuario logueado:
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    // ⚠️ Cuando entra a esta pantalla, carga los datos del usuario
    LaunchedEffect(userId) {
        if (userId != null) {
            viewModel.cargarDatosUsuario(userId)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            ModalDrawerSheet {

                Text(
                    "Menú",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Mi Finca") },
                    selected = false,
                    icon = { Icon(Icons.Default.Grass, contentDescription = null) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("miFinca")
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Mi Cuenta") },
                    selected = false,
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("miCuenta")
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Cerrar Sesión") },
                    selected = false,
                    icon = { Icon(Icons.Default.Logout, contentDescription = null) },
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        scope.launch { drawerState.close() }
                        navController.navigate("welcome") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

    ) {

        Scaffold(
            topBar = {
                FincaTopBar(
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    },
                    onSearchClick = {}
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("crearAnimal") },
                    containerColor = Color(0xFFE94444)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                }
            },
            bottomBar = {
                FincaBottomBar(
                    currentRoute = "inicio",
                    onNavigate = { nuevaRuta ->
                        navController.navigate(nuevaRuta)
                    }
                )
            }
        ) { padding ->

            // 🟦 Si no existe finca, muestra mensaje
            if (!viewModel.fincaCreada) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Aún no tienes una finca registrada.",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { navController.navigate("crearFinca") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE94444))
                    ) {
                        Text("Crear Finca")
                    }
                }
            } else {

                // 🟩 Si sí existe finca, muestra métricas
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // --- MÉTRICAS ---
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        MetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Machos",
                            value = viewModel.totalMachos.toString(),
                            icon = { Icon(Icons.Default.Male, "Machos") }
                        )
                        MetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Hembras",
                            value = viewModel.totalHembras.toString(),
                            icon = { Icon(Icons.Default.Female, "Hembras") }
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        MetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Promedio de ganancia de peso",
                            value = "${viewModel.gananciaPeso} kg",
                            subtitle = "--",
                            icon = { Icon(Icons.Default.TrendingUp, "Ganancia peso") }
                        )
                        MetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Total de carne",
                            value = "${viewModel.totalCarne} kg",
                            subtitle = "--",
                            icon = { Icon(Icons.Default.Fastfood, "Total carne") }
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        MetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Producción total de leche",
                            value = "${viewModel.totalLeche} L",
                            subtitle = "--",
                            icon = { Icon(Icons.Default.LocalDrink, "Total leche") }
                        )
                        MetricCard(
                            modifier = Modifier.weight(1f),
                            title = "Animales producidos",
                            value = viewModel.animalesProducidos.toString(),
                            icon = { Icon(Icons.Default.Pets, "Animales producidos") }
                        )
                    }
                }
            }
        }
    }
}
