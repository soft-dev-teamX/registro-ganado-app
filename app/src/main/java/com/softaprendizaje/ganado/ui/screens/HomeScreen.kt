package com.softaprendizaje.ganado.ui.screens

import com.softaprendizaje.ganado.ui.components.FincaBottomBar
import com.softaprendizaje.ganado.ui.components.FincaTopBar
// Importamos la nueva y mejorada MetricCard
// import com.softaprendizaje.ganado.ui.components.MetricCard
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.softaprendizaje.ganado.ui.viewmodels.GanadoViewModel
import com.softaprendizaje.ganado.ui.theme.main_blue // ⬅️ Importamos main_blue
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
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
        // 1. Mejoramos el diseño del Drawer
        drawerContent = {
            ModalDrawerSheet {

                // --- Encabezado del Drawer ---
                DrawerHeader(viewModel.nombreUsuario) // Usamos el nombre del usuario

                // --- Ítems del Menú ---
                NavigationDrawerItem(
                    label = { Text("Mi Finca") },
                    selected = false,
                    icon = { Icon(Icons.Default.Grass, contentDescription = null, tint = main_blue) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("miFinca")
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Mi Cuenta") },
                    selected = false,
                    icon = { Icon(Icons.Default.Person, contentDescription = null, tint = main_blue) },
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("miCuenta")
                    }
                )

                Divider(modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp))

                NavigationDrawerItem(
                    label = { Text("Cerrar Sesión") },
                    selected = false,
                    icon = { Icon(Icons.Default.Logout, contentDescription = null, tint = Color.Red.copy(alpha = 0.8f)) },
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
            // 2. Usamos FincaTopBar, asumiendo que ya fue estilizada con main_blue
            topBar = {
                FincaTopBar(
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    },
                    onSearchClick = {}
                )
            },
            // 3. Mejoramos el FAB (igual que en Animales, usando main_blue)
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("crearAnimal") },
                    containerColor = main_blue, // Usamos main_blue para el FAB
                    shape = RoundedCornerShape(16.dp) // Forma moderna
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar", tint = Color.White)
                }
            },
            // 4. Usamos FincaBottomBar (asumiendo que ya fue estilizada)
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
                        "Bienvenido ${viewModel.nombreUsuario}. Aún no tienes una finca registrada.",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate("crearFinca") },
                        colors = ButtonDefaults.buttonColors(containerColor = main_blue), // Botón con main_blue
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("Registrar Mi Finca")
                    }
                }
            } else {

                // 🟩 Si sí existe finca, muestra métricas
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // --- TÍTULO DE BIENVENIDA ---
                    Text(
                        "Hola, ${viewModel.nombreUsuario}!",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // --- MENSAJE DE ESTADO DE LA FINCA ---
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = main_blue.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Tu finca está activa. Echemos un vistazo a los datos.",
                            modifier = Modifier.padding(12.dp),
                            color = main_blue
                        )
                    }

                    // --- AGRUPACIÓN DE MÉTRICAS ---

                    // Grupo 1: Totales de Animales
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                            MetricCard(
                                modifier = Modifier.weight(1f),
                                title = "Machos",
                                value = viewModel.totalMachos.toString(),
                                icon = { Icon(Icons.Default.Male, "Machos") },
                                highlightColor = main_blue
                            )
                            MetricCard(
                                modifier = Modifier.weight(1f),
                                title = "Hembras",
                                value = viewModel.totalHembras.toString(),
                                icon = { Icon(Icons.Default.Female, "Hembras") },
                                highlightColor = main_blue
                            )
                        }
                    }

                    // Grupo 2: Pesos y Leche
                    Text("Métricas de Producción", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        MetricCard(
                            title = "Ganancia de Peso Promedio",
                            value = "${viewModel.gananciaPeso} kg",
                            subtitle = "Promedio",
                            icon = { Icon(Icons.Default.TrendingUp, "Ganancia peso") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        MetricCard(
                            title = "Total de Carne Producida",
                            value = "${viewModel.totalCarne} kg",
                            subtitle = "Estimado",
                            icon = { Icon(Icons.Default.Fastfood, "Total carne") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        MetricCard(
                            title = "Producción Total de Leche",
                            value = "${viewModel.totalLeche} L",
                            subtitle = "Estimado",
                            icon = { Icon(Icons.Default.LocalDrink, "Total leche") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Grupo 3: Otros Animales
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Otros Animales",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                        // ✔ HEMBRAS que han producido (booleano)
                        MetricCard(
                            title = "Hembras Producidas",
                            value = viewModel.animalesProducidos.toString(),
                            subtitle = "Han parido",
                            icon = { Icon(Icons.Default.CheckCircle, "Hembras que produjeron") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // ✔ TOTAL DE CRÍAS producidas (suma númeroCrias)
                        MetricCard(
                            title = "Crías Producidas",
                            value = viewModel.totalCriasProducidas.toString(),
                            subtitle = "Nacimientos",
                            icon = { Icon(Icons.Default.Pets, "Total crías producidas") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Componentes Reutilizables Mejorados
// -------------------------------------------------------------------------------------------------

@Composable
fun DrawerHeader(userName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(main_blue)
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Icon(
            Icons.Default.AccountCircle,
            contentDescription = "Usuario",
            tint = Color.White,
            modifier = Modifier.size(56.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Bienvenido,",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White.copy(alpha = 0.8f)
        )
        Text(
            userName,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
    }
}

// ⚠️ Usaremos esta definición de MetricCard para el nuevo estilo.
// Reemplaza la que tienes en 'com.softaprendizaje.ganado.ui.components.MetricCard'
@Composable
fun MetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subtitle: String? = null,
    icon: @Composable () -> Unit,
    highlightColor: Color = MaterialTheme.colorScheme.primary
) {
    Card(
        modifier = modifier.heightIn(min = 100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Fila de Ícono y Título
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Ícono circular con color de resaltado
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(highlightColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    CompositionLocalProvider(LocalContentColor provides highlightColor) {
                        icon()
                    }
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.height(8.dp))

            // Valor Principal (Grande y Destacado)
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = highlightColor // Valor destacado con el color de la marca
            )

            // Subtítulo (Opcional)
            if (subtitle != null) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}