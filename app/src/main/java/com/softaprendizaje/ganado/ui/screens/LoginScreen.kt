package com.softaprendizaje.ganado.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔹 Botón Volver
        TextButton(onClick = { navController.popBackStack() }) {
            Text("← Volver", color = Color(0xFF6366F1))
        }

        Text(
            text = "Iniciar sesión",
            fontSize = 22.sp,
            color = Color(0xFF6366F1),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña *") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        // 🔹 Olvidé contraseña
        Text(
            text = "¿Olvidaste tu contraseña?",
            color = Color(0xFFEF4444),
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 4.dp)
                .clickable {
                    // (opcional) más adelante puedes implementar recuperación
                }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Botón de inicio de sesión
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    isLoading = true
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                mensaje = "Inicio de sesión exitoso ✅"
                                // 🔹 Ir a pantalla principal (Home)
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                mensaje = "Error: ${task.exception?.message}"
                            }
                        }
                } else {
                    mensaje = "Por favor llena los campos obligatorios"
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
            } else {
                Text("Ingresar", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 Enlace para ir al registro
        Text(
            text = "Registrarme",
            color = Color(0xFFEF4444),
            modifier = Modifier.clickable {
                navController.navigate("register")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (mensaje.isNotEmpty()) {
            Text(text = mensaje, color = Color.Gray, fontSize = 14.sp)
        }
    }
}
