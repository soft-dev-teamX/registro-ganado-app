package com.softaprendizaje.ganado.viewmodels

import android.content.Context
import android.os.Environment
import androidx.lifecycle.ViewModel
import com.softaprendizaje.ganado.ui.data.Animal
import java.io.File
import java.io.FileOutputStream

class ReportesViewModel : ViewModel() {

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

        // UTF-8 + BOM para Excel
        val bom = "\uFEFF"  // EF BB BF

        return bom + header + body
    }

}
