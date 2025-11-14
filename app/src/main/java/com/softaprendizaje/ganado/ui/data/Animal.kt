package com.softaprendizaje.ganado.ui.data


// 1. Modelo de Datos
// Define qué es un animal en tu app
data class Animal(
    val id: String = "",
    val numero: String = "",
    val raza: String = "",
    val fechaNacimiento: String = "",
    val proposito: String = "",
    val esMacho: Boolean = false,

    val carne: Double = 0.0,
    val leche: Double = 0.0,
    val gananciaPeso: Double = 0.0,
    val producido: Boolean = false
)
