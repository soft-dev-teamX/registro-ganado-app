package com.softaprendizaje.ganado.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.softaprendizaje.ganado.ui.theme.main_blue // ⬅️ Importamos main_blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    // 1. ESTADOS
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            // Añadimos el padding vertical y horizontal al contenedor principal
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- 1. HEADER & LOGO/ICONO ---
        // Simulación de un logo o icono de bienvenida (opcional)
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(main_blue.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Registro",
                tint = main_blue,
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Crea tu cuenta",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = main_blue
        )
        Text(
            "Ingresa tus datos para empezar a gestionar tu ganado.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(32.dp))

        // --- 2. CAMPOS DE FORMULARIO ---

        // Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre *") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = main_blue) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp) // Estilo redondeado
        )

        // Apellido
        OutlinedTextField(
            value = apellido,
            onValueChange = { apellido = it },
            label = { Text("Apellido *") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = main_blue) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp)
        )

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico *") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = main_blue) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp)
        )

        // Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña *") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = main_blue) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- 3. MENSAJES Y BOTÓN ---

        if (errorMessage.isNotEmpty()) {
            // Estilo para el mensaje de error
            Text(
                errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (isLoading) {
            CircularProgressIndicator(color = main_blue) // Aplicamos main_blue al indicador
        } else {
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank() || nombre.isBlank() || apellido.isBlank()) {
                        errorMessage = "Por favor, completa todos los campos obligatorios."
                        return@Button
                    }
                    isLoading = true
                    errorMessage = ""

                    // Lógica de Firebase: INICIO

                    // 1. Crea el usuario en Firebase Auth
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userId = task.result?.user?.uid
                                if (userId != null) {
                                    // 2. Guarda la información adicional en Realtime Database
                                    val userMap = hashMapOf(
                                        "nombre" to nombre,
                                        "apellido" to apellido,
                                        "email" to email
                                    )
                                    FirebaseDatabase.getInstance().getReference("Usuarios").child(userId)
                                        .setValue(userMap)
                                        .addOnCompleteListener { dbTask ->
                                            isLoading = false
                                            if (dbTask.isSuccessful) {
                                                // 3. Si todo sale bien, navega
                                                onRegisterSuccess()
                                            } else {
                                                errorMessage = dbTask.exception?.message ?: "Error al guardar datos."
                                            }
                                        }
                                }
                            } else {
                                isLoading = false
                                errorMessage = task.exception?.message ?: "Error al registrar."
                                Toast.makeText(context, "Error de registro.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    // Lógica de Firebase: FIN
                },
                // Estilo del botón (color principal, altura, forma)
                colors = ButtonDefaults.buttonColors(containerColor = main_blue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Registrarse", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Opción para ir a Login (opcional, si tienes una ruta de login)
        TextButton(onClick = onLoginClick, enabled = !isLoading) {
            Text("¿Ya tienes una cuenta? Inicia sesión", color = main_blue)
        }
    }
}