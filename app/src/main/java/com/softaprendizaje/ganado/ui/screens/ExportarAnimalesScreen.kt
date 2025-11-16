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
            context.contentResolver.openOutputStream(uri)?.use { output ->
                output.write(csvData.value.toByteArray())
            }
            Toast.makeText(context, "Archivo guardado correctamente", Toast.LENGTH_LONG).show()
        }
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Exportar Animales") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text(
                "Exportación de Animales",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                "Genera un archivo CSV con todos los animales registrados.",
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = {
                    val csv = reportesViewModel.generarCSV(viewModel.animales)

                    if (csv.isEmpty()) {
                        Toast.makeText(context, "No hay animales para exportar.", Toast.LENGTH_LONG).show()
                    } else {
                        csvData.value = csv
                        createDocumentLauncher.launch("animales_${System.currentTimeMillis()}.csv")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.FileDownload, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Exportar a CSV")
            }
        }
    }
}
