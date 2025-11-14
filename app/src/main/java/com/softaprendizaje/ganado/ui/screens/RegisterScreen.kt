package com.softaprendizaje.ganado.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit
    // ❌ ELIMINAMOS la referencia al ViewModel
) {
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
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registrarse", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre *") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido *") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo electrónico *") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña *") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank() || nombre.isBlank()) {
                        errorMessage = "Por favor, completa todos los campos obligatorios."
                        return@Button
                    }
                    isLoading = true
                    errorMessage = ""
                    // ✅ 1. Crea el usuario en Firebase Auth
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
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }
        }
    }
}
