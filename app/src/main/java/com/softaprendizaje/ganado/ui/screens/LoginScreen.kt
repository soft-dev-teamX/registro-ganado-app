package com.softaprendizaje.ganado.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pets // Nuevo ícono para representar la app de ganado
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
import com.softaprendizaje.ganado.ui.theme.main_blue // ⬅️ Importamos main_blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            // Padding horizontal generoso
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center, // Centrado en el medio de la pantalla
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- 1. LOGO/ICONO DE BIENVENIDA ---
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(main_blue.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Pets, // Ícono relacionado con animales/ganado
                contentDescription = "Ganado",
                tint = main_blue,
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        // --- 2. TÍTULO ---
        Text(
            "Bienvenido de nuevo",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
            color = main_blue
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Inicia sesión para acceder a tu finca.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(32.dp))

        // --- 3. CAMPOS DE TEXTO ---

        // Correo electrónico
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico *") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = main_blue) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp) // Estilo redondeado
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
            shape = RoundedCornerShape(12.dp) // Estilo redondeado
        )
        Spacer(modifier = Modifier.height(24.dp))

        // --- 4. MENSAJES Y BOTÓN PRINCIPAL ---

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (isLoading) {
            CircularProgressIndicator(color = main_blue)
        } else {
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Por favor, completa todos los campos."
                        return@Button
                    }
                    isLoading = true
                    errorMessage = ""
                    // Lógica de Firebase: INICIO
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                onLoginClick()
                            } else {
                                errorMessage = task.exception?.message ?: "Error desconocido."
                                Toast.makeText(context, "Error de autenticación.", Toast.LENGTH_SHORT).show()
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
                Text("Ingresar", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- 5. BOTÓN DE REGISTRO ---
        TextButton(onClick = onRegisterClick, enabled = !isLoading) {
            Text("¿No tienes cuenta? Regístrate", color = main_blue)
        }
    }
}