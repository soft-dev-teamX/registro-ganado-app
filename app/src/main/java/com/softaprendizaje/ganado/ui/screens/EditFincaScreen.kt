package com.softaprendizaje.ganado.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.softaprendizaje.ganado.ui.viewmodels.GanadoViewModel
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFincaScreen(
    onSaveSuccess: () -> Unit,
    viewModel: GanadoViewModel = viewModel()
) {
    val fincaData = viewModel.finca   // ← CORREGIDO

    val initialFinca = fincaData ?: com.softaprendizaje.ganado.ui.data.Finca()
    val decimalFormat = remember { DecimalFormat("#.##") }

    // Estados para los campos (todos como String)
    var nombre by remember { mutableStateOf(initialFinca.nombre) }
    var proposito by remember { mutableStateOf(initialFinca.proposito) }
    var ubicacion by remember { mutableStateOf(initialFinca.ubicacion) }

    var area by remember { mutableStateOf(decimalFormat.format(initialFinca.area)) }
    var areaGanado by remember { mutableStateOf(decimalFormat.format(initialFinca.areaGanado)) }

    var isLoading by remember { mutableStateOf(false) }

    // Sincronizar datos cuando fincaData cambie
    LaunchedEffect(fincaData) {
        val current = fincaData ?: return@LaunchedEffect
        nombre = current.nombre
        proposito = current.proposito
        ubicacion = current.ubicacion
        area = decimalFormat.format(current.area)
        areaGanado = decimalFormat.format(current.areaGanado)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Finca", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onSaveSuccess) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Información Principal",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de la Finca") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = proposito,
                onValueChange = { proposito = it },
                label = { Text("Propósito") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Área y Ubicación",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = area,
                    onValueChange = { area = it },
                    label = { Text("Área Total (ha)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = areaGanado,
                    onValueChange = { areaGanado = it },
                    label = { Text("Área de Ganado (ha)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = ubicacion,
                onValueChange = { ubicacion = it },
                label = { Text("Ubicación (Ej: País, Ciudad)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(48.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        isLoading = true
                        viewModel.updateFincaData(
                            nombre,
                            proposito,
                            ubicacion,
                            area,
                            areaGanado
                        )
                        isLoading = false
                        onSaveSuccess()
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = nombre.isNotBlank() &&
                            proposito.isNotBlank() &&
                            ubicacion.isNotBlank()
                ) {
                    Text("Guardar Cambios de Finca")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
