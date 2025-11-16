package com.softaprendizaje.ganado.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.softaprendizaje.ganado.ui.viewmodels.GanadoViewModel
import com.softaprendizaje.ganado.viewmodels.ReportesViewModel

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.softaprendizaje.ganado.ui.theme.main_blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportarAnimalesScreen(
    viewModel: GanadoViewModel = viewModel(),
    reportesViewModel: ReportesViewModel = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val csvData = remember { mutableStateOf("") }

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        if (uri != null) {

            //Guardar Archivo
            context.contentResolver.openOutputStream(uri)?.use { output ->
                output.write(csvData.value.toByteArray())
            }

            // Registrar en historial
            reportesViewModel.registrarArchivoDescargado(
                nombre = uri.lastPathSegment ?: "reporte.csv"
            )

            // Usamos Snackbar o un Toast más moderno si es posible, pero mantenemos el original
            Toast.makeText(context, "Archivo guardado correctamente", Toast.LENGTH_LONG).show()
        }
    }


    Scaffold(
        topBar = {
            // 1. TopAppBar estilizado con main_blue
            CenterAlignedTopAppBar(
                title = { Text("Exportar Animales", color = Color.White, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = main_blue
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 20.dp) // Padding más amplio
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp) // Mayor espacio entre secciones
        ) {

            // --- 2. Ícono de Reporte Destacado ---
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(main_blue.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.FileDownload,
                    contentDescription = "Exportar",
                    tint = main_blue,
                    modifier = Modifier.size(48.dp)
                )
            }

            // --- Títulos y Descripción ---
            Text(
                "Exportación de Datos",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                "Genera un archivo CSV que contiene el inventario completo de todos los animales registrados en tu finca. Este archivo puede ser usado para análisis externos.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // --- Botón de Acción Principal ---
            Button(
                onClick = {
                    val csv = reportesViewModel.generarCSV(viewModel.animales)

                    if (csv.isEmpty()) {
                        Toast.makeText(context, "No hay animales para exportar.", Toast.LENGTH_LONG).show()
                    } else {
                        csvData.value = csv
                        // Naming convention mejorada con fecha
                        createDocumentLauncher.launch("animales_reporte_${System.currentTimeMillis()}.csv")
                    }
                },
                // 3. Botón estilizado con main_blue
                colors = ButtonDefaults.buttonColors(containerColor = main_blue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(12.dp))
                Text("Exportar Inventario CSV", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(Modifier.height(16.dp))

            // Información Adicional/Advertencia
            Text(
                "Nota: El archivo CSV contendrá la información básica de identificación y métricas actuales de cada animal.",
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray
            )
        }
    }
}