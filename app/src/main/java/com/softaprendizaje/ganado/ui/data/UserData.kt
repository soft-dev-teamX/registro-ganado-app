package com.softaprendizaje.ganado.ui.data

// ✅ Este modelo está bien para el mapeo con Realtime Database si no hay campos nulos
data class UserData(
    val nombre: String = "",
    val apellido: String = "",
    val email: String = "",
    val pais: String = "",
    val region: String = "",
    val ciudad: String = "",
    val telefono: String = "",
    val fotoPerfilUrl: String = ""
)