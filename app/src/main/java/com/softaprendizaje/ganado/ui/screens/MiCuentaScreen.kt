package com.softaprendizaje.ganado.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.softaprendizaje.ganado.ui.viewmodels.MiCuentaViewModel

@Composable
fun MiCuentaScreen() {

    val viewModel: MiCuentaViewModel = viewModel()
    val userData by viewModel.userData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // FOTO DE PERFIL
        if (userData.fotoPerfilUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(userData.fotoPerfilUrl),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("Sin foto")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // INFORMACIÓN PERSONAL
        Text("Información Personal", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(10.dp))

        InfoItem("Nombre", userData.nombre)
        InfoItem("Apellido", userData.apellido)
        InfoItem("Correo", userData.email)

        Spacer(modifier = Modifier.height(20.dp))

        // GENERAL
        Text("General", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(10.dp))

        InfoItem("País", userData.pais)
        InfoItem("Región", userData.region)
        InfoItem("Ciudad", userData.ciudad)
        InfoItem("Teléfono", userData.telefono)
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.labelLarge)
        Text(
            text = if (value.isNotEmpty()) value else "",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(6.dp))
    }
}
