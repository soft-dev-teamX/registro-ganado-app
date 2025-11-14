package com.softaprendizaje.ganado.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday // Para el ícono de fecha
import androidx.compose.material.icons.filled.ArrowDropDown // Para el Dropdown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.softaprendizaje.ganado.ui.data.Animal
import com.softaprendizaje.ganado.ui.viewmodels.GanadoViewModel
import java.util.UUID

import android.app.DatePickerDialog
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAnimalScreen(
    onAnimalCreated: () -> Unit // Cambié el nombre del parámetro a algo más claro
) {
    // 1. ESTADOS DE LOS CAMPOS
    val viewModel: GanadoViewModel = viewModel()
    val scrollState = rememberScrollState()

    // Campos de Identificación
    var numero by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }

    // Campos de Información básica
    var fechaNacimiento by remember { mutableStateOf("") } // Usaremos un diálogo de fecha para esto
    var numeroLote by remember { mutableStateOf("") }
    var propositoAnimal by remember { mutableStateOf("Doble propósito") } // Valor inicial
    var isDropdownExpanded by remember { mutableStateOf(false) } // Para el Dropdown
    val propositoOptions = listOf("Doble propósito", "Carne", "Leche", "Cría")
    var esMacho by remember { mutableStateOf(true) }


    // Campo de Otros
    var observaciones by remember { mutableStateOf("") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Animal") },
                navigationIcon = {
                    IconButton(onClick = onAnimalCreated) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState), // Habilitamos el scroll
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // --- 🐮 SECCIÓN DE IDENTIFICACIÓN ---
            FormSectionHeader(title = "Identificación")

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = numero,
                onValueChange = { numero = it },
                label = { Text("Número del animal *") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = raza,
                onValueChange = { raza = it },
                label = { Text("Raza del animal *") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- 🗓️ SECCIÓN INFORMACIÓN BÁSICA ---
            FormSectionHeader(title = "Información básica")

            Spacer(modifier = Modifier.height(8.dp))
            // Campo de Fecha de nacimiento (simulado como TextField clickable)
            OutlinedTextField(
                value = fechaNacimiento,
                onValueChange = { },
                label = { Text("Fecha de nacimiento *") },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = "Seleccionar fecha",
                        modifier = Modifier.clickable {
                            val datePicker = DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    fechaNacimiento = "$dayOfMonth/${month + 1}/$year"
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            )
                            datePicker.show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = numeroLote,
                onValueChange = { numeroLote = it },
                label = { Text("Número de lote") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Campo de Propósito Animal (Dropdown)
            DropdownMenuField(
                label = "Propósito Animal *",
                selectedValue = propositoAnimal,
                options = propositoOptions,
                onSelected = { propositoAnimal = it }
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text("Sexo del animal *", style = MaterialTheme.typography.titleMedium)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = esMacho,
                        onClick = { esMacho = true }
                    )
                    Text("Macho")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = !esMacho,
                        onClick = { esMacho = false }
                    )
                    Text("Hembra")
                }
            }


            Spacer(modifier = Modifier.height(24.dp))

            // --- 📝 SECCIÓN OTROS ---
            FormSectionHeader(title = "Otros")

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = observaciones,
                onValueChange = { observaciones = it },
                label = { Text("Condición de salud y observaciones") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                singleLine = false // Para que sea un campo de texto multi-línea
            )

            // Espacio para empujar el botón al fondo si hay mucho espacio, si no, es solo un separador.
            Spacer(modifier = Modifier.height(24.dp))

            // --- ✅ BOTÓN CREAR ---
            Button(
                onClick = {
                    if (numero.isBlank() || raza.isBlank() || fechaNacimiento.isBlank()) {
                        // TODO: Mostrar un Snackbar de error
                        return@Button
                    }

                    val nuevoAnimal = Animal(
                        id = UUID.randomUUID().toString(),
                        numero = numero,
                        raza = raza,
                        fechaNacimiento = fechaNacimiento,
                        proposito = propositoAnimal,
                        esMacho = esMacho,
                        )
                    viewModel.agregarAnimal(nuevoAnimal)

                    onAnimalCreated() // Regresa a la pantalla anterior.
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE94444)),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text("Crear", modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

// --- Componente Reutilizable (Encabezado de Sección) ---
@Composable
fun FormSectionHeader(title: String) {
    Surface(
        color = MaterialTheme.colorScheme.primary, // Fondo azul de la maqueta
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
    }
}

// --- Componente Reutilizable (Dropdown para Propósito Animal) ---
@Composable
fun DropdownMenuField(
    label: String,
    selectedValue: String,
    options: List<String>,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedValue,
            onValueChange = { },
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onSelected(selectionOption)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}