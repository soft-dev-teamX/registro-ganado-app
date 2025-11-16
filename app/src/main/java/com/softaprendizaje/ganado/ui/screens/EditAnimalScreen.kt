package com.softaprendizaje.ganado.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.softaprendizaje.ganado.ui.data.Animal
import com.softaprendizaje.ganado.ui.theme.main_blue
import com.softaprendizaje.ganado.ui.viewmodels.GanadoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAnimalScreen(
    navController: NavController,
    animalId: String,
    viewModel: GanadoViewModel = viewModel()
){
    var animalToEdit by remember { mutableStateOf<Animal?>(null) }

    // Estados del formulario
    var numero by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var proposito by remember { mutableStateOf("Leche") }
    var esMacho by remember { mutableStateOf(false) }
    var fechaNacimiento by remember { mutableStateOf("DD/MM/AAAA") }

    // Campos dinámicos
    var leche by remember { mutableStateOf("0.0") }
    var carne by remember { mutableStateOf("0.0") }
    var gananciaPeso by remember { mutableStateOf("0.0") }
    var producido by remember { mutableStateOf(false) }
    var numeroCrias by remember { mutableStateOf("0") }

    var errorMensaje by remember { mutableStateOf<String?>(null) }

    // Cargar datos existentes
    LaunchedEffect(animalId, viewModel.animales) {
        val foundAnimal = viewModel.animales.find { it.id == animalId }
        animalToEdit = foundAnimal

        if (foundAnimal != null) {
            numero = foundAnimal.numero
            raza = foundAnimal.raza
            proposito = foundAnimal.proposito
            esMacho = foundAnimal.esMacho
            fechaNacimiento = foundAnimal.fechaNacimiento

            leche = foundAnimal.leche.toString()
            carne = foundAnimal.carne.toString()
            gananciaPeso = foundAnimal.gananciaPeso.toString()

            producido = foundAnimal.producido
            numeroCrias = foundAnimal.numeroCrias.toString()

        } else {
            errorMensaje = "Animal no encontrado con ID: $animalId"
        }
    }

    val propositoOptions = listOf("Leche", "Carne", "Doble Propósito", "Cría")

    fun handleUpdateAnimal() {
        if (animalToEdit == null) {
            errorMensaje = "Error: No hay animal para actualizar."
            return
        }

        val updated = animalToEdit!!.copy(
            numero = numero,
            raza = raza,
            proposito = proposito,
            esMacho = esMacho,
            fechaNacimiento = fechaNacimiento,

            leche = if (proposito == "Leche" || proposito == "Doble Propósito")
                leche.toDoubleOrNull() ?: 0.0 else 0.0,

            carne = if (proposito == "Carne" || proposito == "Doble Propósito")
                carne.toDoubleOrNull() ?: 0.0 else 0.0,

            gananciaPeso = if (proposito != "Cría")
                gananciaPeso.toDoubleOrNull() ?: 0.0 else 0.0,

            producido = if (proposito == "Cría") producido else false,

            numeroCrias = if (proposito == "Cría")
                numeroCrias.toIntOrNull() ?: 0 else 0
        )

        viewModel.updateAnimal(updated)
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Editar Animal", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = main_blue),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        if (errorMensaje != null && animalToEdit == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(errorMensaje!!, color = MaterialTheme.colorScheme.error)
            }
            return@Scaffold
        }

        if (animalToEdit == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = main_blue)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- BÁSICOS ---
            OutlinedTextField(
                value = numero,
                onValueChange = { numero = it },
                label = { Text("Número/Identificador") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = raza,
                onValueChange = { raza = it },
                label = { Text("Raza") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = fechaNacimiento,
                onValueChange = { fechaNacimiento = it },
                label = { Text("Fecha de nacimiento") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            Spacer(Modifier.height(16.dp))

            // --- PROPÓSITO ---
            var expanded by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = proposito,
                onValueChange = {},
                label = { Text("Propósito") },
                trailingIcon = {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.clickable { expanded = true }
                    )
                },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                propositoOptions.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            proposito = it
                            expanded = false
                        }
                    )
                }
            }

            // --- CAMPOS DINÁMICOS ---

            // Leche
            if (proposito == "Leche" || proposito == "Doble Propósito") {
                OutlinedTextField(
                    value = leche,
                    onValueChange = { leche = it },
                    label = { Text("Producción de leche (L/día)") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
            }

            // Carne
            if (proposito == "Carne" || proposito == "Doble Propósito") {
                OutlinedTextField(
                    value = carne,
                    onValueChange = { carne = it },
                    label = { Text("Rendimiento de carne (kg)") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
            }

            // Ganancia de peso (todos excepto cría)
            if (proposito != "Cría") {
                OutlinedTextField(
                    value = gananciaPeso,
                    onValueChange = { gananciaPeso = it },
                    label = { Text("Ganancia de peso (kg/mes)") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
            }

            // Campos de cría
            if (proposito == "Cría") {

                // Número de crías
                OutlinedTextField(
                    value = numeroCrias,
                    onValueChange = { numeroCrias = it },
                    label = { Text("Número total de crías producidas") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                // Producido: útil como "¿Parió este año?"
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("¿Ha producido (este año)?")
                    Switch(
                        checked = producido,
                        onCheckedChange = { producido = it }
                    )
                }
            }

            // Sexo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Sexo: ${if (esMacho) "Macho" else "Hembra"}")
                Switch(checked = esMacho, onCheckedChange = { esMacho = it })
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = ::handleUpdateAnimal,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = main_blue)
            ) {
                Text("Guardar Cambios")
            }
        }
    }
}
