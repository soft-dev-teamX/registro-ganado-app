package com.softaprendizaje.ganado.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun RegisterScreen(
    navController: NavController
) {
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().getReference("usuarios")

    var email by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var pais by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var esPropietario by remember { mutableStateOf(false) }

    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔹 Botón "Volver"
        TextButton(onClick = { navController.popBackStack() }) {
            Text("← Volver", color = Color(0xFF6366F1))
        }

        Text(
            text = "Registrarse",
            fontSize = 22.sp,
            color = Color(0xFF6366F1),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // 🧩 Campos del formulario (igual que antes)
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Correo electrónico *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = nombre, onValueChange = { nombre = it },
            label = { Text("Nombre *") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = apellido, onValueChange = { apellido = it },
            label = { Text("Apellido *") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Contraseña *") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = pais, onValueChange = { pais = it },
            label = { Text("País") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = telefono, onValueChange = { telefono = it },
            label = { Text("Teléfono / Celular") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = region, onValueChange = { region = it },
            label = { Text("Región") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = ciudad, onValueChange = { ciudad = it },
            label = { Text("Ciudad") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = esPropietario, onCheckedChange = { esPropietario = it })
            Text("Soy propietario de una finca")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Botón de registro
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val uid = auth.currentUser?.uid
                                val usuario = mapOf(
                                    "uid" to uid,
                                    "nombre" to nombre,
                                    "apellido" to apellido,
                                    "pais" to pais,
                                    "telefono" to telefono,
                                    "region" to region,
                                    "ciudad" to ciudad,
                                    "esPropietario" to esPropietario
                                )
                                if (uid != null) {
                                    database.child(uid).setValue(usuario)
                                    mensaje = "Registro exitoso ✅"
                                    // 🔹 Al completar, volver a la pantalla de login
                                    navController.navigate("login") {
                                        popUpTo("register") { inclusive = true }
                                    }
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
            Text("Registrarse", color = Color.White)
        }

        if (mensaje.isNotEmpty()) {
            Text(text = mensaje, color = Color.Gray, fontSize = 14.sp)
        }
    }
}
