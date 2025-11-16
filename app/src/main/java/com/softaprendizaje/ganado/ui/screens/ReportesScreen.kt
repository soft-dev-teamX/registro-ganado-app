package com.softaprendizaje.ganado.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.softaprendizaje.ganado.ui.components.MetricCard
import com.softaprendizaje.ganado.ui.theme.main_blue
import com.softaprendizaje.ganado.ui.viewmodels.GanadoViewModel
import com.softaprendizaje.ganado.viewmodels.ReportesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportesScreen(
    viewModel: GanadoViewModel = viewModel(),
    reportesViewModel: ReportesViewModel = viewModel(),
    onNavigateTo: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Informes", color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
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
        ) {

            // ==========================
            // 🟦 GENERAR NUEVO INFORME
            // ==========================
            Text(
                "Generar nuevo informe",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            TarjetaGenerarInforme(
                ultimo = reportesViewModel.ultimoReporte.value ?: "--",
                onClick = { onNavigateTo("exportar_animales") }
            )

            Spacer(Modifier.height(24.dp))


            // ==========================
            // 📘 HISTORIAL DE INFORMES
            // ==========================
            Text(
                "Historial de informes",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            if (reportesViewModel.historialReportes.value.isEmpty()) {
                HistorialVacio()
            } else {
                reportesViewModel.historialReportes.value.forEach {
                    Text("• $it")
                }
            }

        }
    }
}



// ===================================================
// 🟩 TARJETA GRANDE “Generar nuevo informe”
// ===================================================
@Composable
fun TarjetaGenerarInforme(
    ultimo: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    Icons.Default.Assessment,
                    contentDescription = null,
                    tint = main_blue,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(Modifier.width(16.dp))

                Column {
                    Text("Generar nuevo informe", style = MaterialTheme.typography.titleMedium)
                    Text("Último informe: $ultimo", style = MaterialTheme.typography.bodySmall)
                }
            }

            Button(onClick = onClick) {
                Text("Empezar")
            }
        }
    }
}


// ===================================================
// 🌱 HISTORIAL VACÍO (como la maqueta)
// ===================================================
@Composable
fun HistorialVacio() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            Icons.Default.Eco,
            contentDescription = null,
            tint = Color(0xFF9CCC65),
            modifier = Modifier.size(70.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "No hay informes generados",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )
    }
}
