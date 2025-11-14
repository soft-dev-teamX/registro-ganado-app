package com.softaprendizaje.ganado.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.softaprendizaje.ganado.ui.viewmodels.GanadoViewModel
import com.softaprendizaje.ganado.ui.theme.main_blue // ⬅️ Importamos main_blue



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearFincaScreen(
    viewModel: GanadoViewModel = viewModel(),
    onFincaCreada: () -> Unit,
    // Agregamos un callback para volver a la pantalla anterior si el usuario cancela
    onBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var proposito by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()


    Scaffold(
        topBar = {
            // 1. TopBar usando main_blue y botón de regreso
            TopAppBar(
                title = { Text("Registrar Finca", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) { // Usamos el nuevo callback
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = main_blue
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp), // Mayor espacio entre campos
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- 2. Ícono de la Finca ---
            Icon(
                Icons.Default.Home,
                contentDescription = "Finca",
                tint = main_blue,
                modifier = Modifier.size(64.dp).padding(bottom = 8.dp)
            )

            // --- Título y Subtítulo ---
            Text(
                "Datos de la Propiedad",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )

            // --- Campos de entrada ---

            // Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    errorMessage = null
                },
                label = { Text("Nombre de la finca *") },
                leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = main_blue) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Propósito
            OutlinedTextField(
                value = proposito,
                onValueChange = { proposito = it },
                label = { Text("Propósito de la finca (e.g., Carne, Leche)") },
                leadingIcon = { Icon(Icons.Default.Grass, contentDescription = null, tint = main_blue) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Área
            OutlinedTextField(
                value = area,
                onValueChange = { area = it.replace(",", ".") }, // Acepta coma o punto decimal
                label = { Text("Área total (hectáreas)") },
                leadingIcon = { Icon(Icons.Default.SquareFoot, contentDescription = null, tint = main_blue) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mensaje de Error
            if (errorMessage != null) {
                Text(
                    errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // --- Botón de Guardar ---
            Button(
                onClick = {
                    if (nombre.isBlank()) {
                        errorMessage = "El nombre de la finca es obligatorio."
                        return@Button
                    }
                    if (area.isNotBlank() && area.toDoubleOrNull() == null) {
                        errorMessage = "Por favor, ingresa un área válida (número decimal)."
                        return@Button
                    }

                    // Lógica de creación
                    viewModel.crearFinca(
                        nombre = nombre,
                        proposito = proposito,
                        area = area.toDoubleOrNull() ?: 0.0
                    )
                    onFincaCreada()
                },
                // 3. Botón con main_blue, grande y moderno
                colors = ButtonDefaults.buttonColors(containerColor = main_blue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Guardar Finca", modifier = Modifier.padding(vertical = 4.dp), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}