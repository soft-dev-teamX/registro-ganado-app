package com.softaprendizaje.ganado.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.softaprendizaje.ganado.ui.data.Animal
import com.softaprendizaje.ganado.ui.viewmodels.GanadoViewModel
import com.softaprendizaje.ganado.ui.theme.main_blue // ⬅️ Importamos main_blue
import java.util.UUID
import android.app.DatePickerDialog
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAnimalScreen(
    onAnimalCreated: () -> Unit
) {
    // 1. ESTADOS DE LOS CAMPOS
    val viewModel: GanadoViewModel = viewModel()
    val scrollState = rememberScrollState()

    // Campos de Identificación
    var numero by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }

    // Campos de Información básica
    var fechaNacimiento by remember { mutableStateOf("") }
    var numeroLote by remember { mutableStateOf("") }
    var propositoAnimal by remember { mutableStateOf("Doble propósito") }
    val propositoOptions = listOf("Doble propósito", "Carne", "Leche", "Cría")
    var esMacho by remember { mutableStateOf(true) }


    // Campo de Otros
    var observaciones by remember { mutableStateOf("") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Animal", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onAnimalCreated) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                // 1. Aplicamos main_blue a la TopBar
                colors = TopAppBarDefaults.topAppBarColors(containerColor = main_blue)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // --- 🐮 SECCIÓN DE IDENTIFICACIÓN ---
            FormSectionHeader(title = "Identificación") // Usará main_blue

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
            FormSectionHeader(title = "Información básica") // Usará main_blue

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de Fecha de nacimiento (con DatePickerDialog)
            OutlinedTextField(
                value = fechaNacimiento,
                onValueChange = { },
                label = { Text("Fecha de nacimiento *") },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = "Seleccionar fecha",
                        // 2. Aplicamos main_blue al ícono de calendario
                        tint = main_blue,
                        modifier = Modifier.clickable {
                            val datePicker = DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    // Formato de fecha dd/mm/yyyy
                                    fechaNacimiento = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            )
                            // 3. Puedes estilizar el DatePicker si usas un tema custom, aquí solo mostramos
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
            // 4. Mejoramos el estilo del texto de la etiqueta Sexo
            Text(
                "Sexo del animal *",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = esMacho,
                        onClick = { esMacho = true },
                        colors = RadioButtonDefaults.colors(selectedColor = main_blue) // 5. Aplicamos main_blue al RadioButton
                    )
                    Text("Macho")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = !esMacho,
                        onClick = { esMacho = false },
                        colors = RadioButtonDefaults.colors(selectedColor = main_blue) // 5. Aplicamos main_blue al RadioButton
                    )
                    Text("Hembra")
                }
            }


            Spacer(modifier = Modifier.height(24.dp))

            // --- 📝 SECCIÓN OTROS ---
            FormSectionHeader(title = "Otros") // Usará main_blue

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = observaciones,
                onValueChange = { observaciones = it },
                label = { Text("Condición de salud y observaciones") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 200.dp), // Usamos heightIn para mejor control visual
                singleLine = false
            )

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

                    onAnimalCreated()
                },
                // 6. Usamos el rojo distintivo y forma redondeada (como en la maqueta de la finca)
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp)
            ) {
                Text("Crear Animal", modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

// --- Componente Reutilizable (Encabezado de Sección) ---
@Composable
fun FormSectionHeader(title: String) {
    Surface(
        // 7. Aplicamos main_blue al fondo del encabezado
        color = main_blue,
        // 8. Añadimos esquinas redondeadas solo arriba para un mejor estilo visual
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp) // Pequeño espacio para separarlo del componente anterior
    ) {
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
    }
}

// --- Componente Reutilizable (Dropdown para Propósito Animal) ---
@OptIn(ExperimentalMaterial3Api::class)
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