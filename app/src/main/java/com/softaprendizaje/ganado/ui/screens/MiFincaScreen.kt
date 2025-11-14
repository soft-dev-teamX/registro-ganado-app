package com.softaprendizaje.ganado.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.softaprendizaje.ganado.ui.viewmodels.GanadoViewModel

@Composable
fun MiFincaScreen(viewModel: GanadoViewModel = viewModel()) {

    val finca = viewModel.finca

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi Finca") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            if (finca == null) {
                Text("No has registrado ninguna finca todavía.")
            } else {
                Text("Nombre: ${finca.nombre}", style = MaterialTheme.typography.titleLarge)
                Text("Propósito: ${finca.proposito}")
                Text("Área: ${finca.area} ha")
            }
        }
    }
}
