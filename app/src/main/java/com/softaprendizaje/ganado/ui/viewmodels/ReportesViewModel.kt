package com.softaprendizaje.ganado.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.softaprendizaje.ganado.ui.data.Animal
import java.text.SimpleDateFormat
import java.util.*

class ReportesViewModel : ViewModel() {

    // Último reporte generado (texto bonito)
    var ultimoReporte = mutableStateOf<String?>(null)
        private set

    // Lista que verá el usuario en la pantalla
    var historialReportes = mutableStateOf<List<String>>(emptyList())
        private set


    // ============================================
    //  🔵 GENERAR CSV + ACTUALIZAR FECHA
    // ============================================
    fun generarCSV(animales: List<Animal>): String {
        if (animales.isEmpty()) return ""

        val header = "ID,Numero,Raza,Sexo,Proposito,FechaNacimiento,Carne,Leche,GananciaPeso,NumeroCrias,Producido\n"

        val body = animales.joinToString("\n") { animal ->
            val sexo = if (animal.esMacho) "Macho" else "Hembra"
            val produjo = if (animal.producido) "Sí" else "No"

            listOf(
                animal.id ?: "",
                animal.numero ?: "",
                animal.raza ?: "",
                sexo,
                animal.proposito ?: "",
                animal.fechaNacimiento ?: "",
                animal.carne.toString(),
                animal.leche.toString(),
                animal.gananciaPeso.toString(),
                animal.numeroCrias.toString(),
                produjo
            ).joinToString(",")
        }

        val bom = "\uFEFF"

        registrarReporte()

        return bom + header + body
    }


    // ============================================
    //  🔵 Guardar fecha del último reporte
    // ============================================
    private fun registrarReporte() {
        val timestamp = obtenerFechaActual()
        ultimoReporte.value = timestamp
    }


    // ============================================
    //  🔵 Registrar archivo descargado (historial)
    // ============================================
    fun registrarArchivoDescargado(nombre: String) {
        historialReportes.value =
            historialReportes.value + "Archivo guardado: $nombre"
    }


    // ============================================
    //  🔵 Fecha bonita
    // ============================================
    private fun obtenerFechaActual(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}
