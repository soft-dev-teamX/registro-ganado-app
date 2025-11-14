package com.softaprendizaje.ganado.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.softaprendizaje.ganado.ui.viewmodels.GanadoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearFincaScreen(
    viewModel: GanadoViewModel = viewModel(),
    onFincaCreada: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var proposito by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Finca") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Campos de entrada ---
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de la finca *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = proposito,
                onValueChange = { proposito = it },
                label = { Text("Propósito de la finca") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = area,
                onValueChange = { area = it },
                label = { Text("Área total (hectáreas)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (nombre.isNotBlank()) {
                        viewModel.crearFinca(
                            nombre = nombre,
                            proposito = proposito,
                            area = area.toDoubleOrNull() ?: 0.0
                        )
                        onFincaCreada()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE94444)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar", modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}
