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
import com.softaprendizaje.ganado.ui.viewmodels.MiCuentaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onSaveSuccess: () -> Unit, // Acción para volver a MiCuentaScreen
    viewModel: MiCuentaViewModel = viewModel()
) {
    // 1. Obtener los datos actuales del ViewModel (como State)
    val userData by viewModel.userData.collectAsState()

    // 2. Estados mutables para los campos de texto
    var nombre by remember { mutableStateOf(userData.nombre) }
    var apellido by remember { mutableStateOf(userData.apellido) }
    var pais by remember { mutableStateOf(userData.pais) }
    var region by remember { mutableStateOf(userData.region) }
    var ciudad by remember { mutableStateOf(userData.ciudad) }
    var telefono by remember { mutableStateOf(userData.telefono) }

    var isLoading by remember { mutableStateOf(false) }

    // 🌟 CLAVE DE LA SOLUCIÓN: Sincronizar el estado de los campos con userData
    LaunchedEffect(userData) {
        // Esta bloque se ejecuta la primera vez y cada vez que userData cambia.
        nombre = userData.nombre
        apellido = userData.apellido
        pais = userData.pais
        region = userData.region
        ciudad = userData.ciudad
        telefono = userData.telefono
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onSaveSuccess) { // Usamos onSaveSuccess para volver
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- ⚠️ Nota: No editar Email aquí ya que es la clave de Firebase Auth ---

            Text(
                "Información Personal",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            // Nombre y Apellido
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    label = { Text("Apellido") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Ubicación y Contacto",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            // País y Región
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = pais,
                    onValueChange = { pais = it },
                    label = { Text("País") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = region,
                    onValueChange = { region = it },
                    label = { Text("Región") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Ciudad y Teléfono
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = ciudad,
                    onValueChange = { ciudad = it },
                    label = { Text("Ciudad") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Botón de Guardar
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        // Se podría añadir validación aquí

                        isLoading = true
                        viewModel.updateUserData(
                            nombre, apellido, pais, region, ciudad, telefono
                        )

                        // En una aplicación de producción, solo volverías a la pantalla anterior
                        // DESPUÉS de recibir la confirmación (callback) de éxito de Firebase.
                        // Por simplicidad de Canvas, volvemos directamente:
                        onSaveSuccess()
                        isLoading = false
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    // Desactivar el botón si los campos están vacíos (o si los datos no han cambiado)
                    enabled = nombre.isNotBlank() && apellido.isNotBlank()
                ) {
                    Text("Guardar Cambios")
                }
            }
        }
    }
}