package com.softaprendizaje.ganado.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.softaprendizaje.ganado.ui.components.MetricCard
import com.softaprendizaje.ganado.ui.theme.main_blue
import com.softaprendizaje.ganado.ui.viewmodels.GanadoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportesScreen(
    viewModel: GanadoViewModel = viewModel(),
    onNavigateTo: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Reportes", color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = main_blue
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // ==========================
            // 🔷 1. Reporte General
            // ==========================
            Text(
                "Reporte General",
                style = MaterialTheme.typography.titleMedium
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                MetricCard(
                    title = "Total Animales",
                    value = viewModel.animales.size.toString(),
                    subtitle = "Inventario completo",
                    icon = { Icon(Icons.Default.Pets, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                MetricCard(
                    title = "Total Leche",
                    value = "${viewModel.totalLeche} L",
                    subtitle = "Producción acumulada",
                    icon = { Icon(Icons.Default.LocalDrink, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                MetricCard(
                    title = "Total Carne",
                    value = "${viewModel.totalCarne} kg",
                    subtitle = "Rendimiento acumulado",
                    icon = { Icon(Icons.Default.LunchDining, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                MetricCard(
                    title = "Ganancia de Peso",
                    value = "${viewModel.gananciaPeso} kg",
                    subtitle = "Acumulado",
                    icon = { Icon(Icons.Default.MonitorWeight, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ==========================
            // 🔶 2. Reproducción
            // ==========================
            Text(
                "Reproducción",
                style = MaterialTheme.typography.titleMedium
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                MetricCard(
                    title = "Hembras que han Parido",
                    value = viewModel.animalesProducidos.toString(),
                    subtitle = "Hembras productivas",
                    icon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                MetricCard(
                    title = "Total Crías Producidas",
                    value = viewModel.totalCriasProducidas.toString(),
                    subtitle = "Nacimientos registrados",
                    icon = { Icon(Icons.Default.Pets, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ==========================
            // 📊 3. Botones de Reportes
            // ==========================
            Text(
                "Acciones",
                style = MaterialTheme.typography.titleMedium
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                Button(
                    onClick = { onNavigateTo("exportar_animales") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.TableView, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Exportar Reporte (CSV/Excel)")
                }

            }

            Spacer(Modifier.height(30.dp))
        }
    }
}
