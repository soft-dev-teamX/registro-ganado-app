package com.softaprendizaje.ganado.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.softaprendizaje.ganado.ui.data.UserData
import com.softaprendizaje.ganado.ui.viewmodels.MiCuentaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiCuentaScreen(
    onNavigateBack: () -> Unit // Asumimos que necesitas una función para volver
) {
    val viewModel: MiCuentaViewModel = viewModel()
    val userData by viewModel.userData.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Cuenta", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- 🖼️ FOTO DE PERFIL Y CABECERA ---
            HeaderProfile(userData = userData)

            Spacer(modifier = Modifier.height(24.dp))

            // --- 💳 CARD: INFORMACIÓN PERSONAL ---
            DataCard(
                title = "Información Personal",
                // TODO: Implementar lógica de navegación a edición
                onEditClick = { /* navController.navigate("editarPerfil") */ }
            ) {
                // Organizado en 2 columnas (Nombre/Apellido y Correo)
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Nombre
                        InfoItem(label = "Nombre", value = userData.nombre, modifier = Modifier.weight(1f))
                        // Apellido
                        InfoItem(label = "Apellidos", value = userData.apellido, modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    // Correo Electrónico (ocupa todo el ancho)
                    InfoItem(label = "Correo Electrónico", value = userData.email, modifier = Modifier.fillMaxWidth())
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 💳 CARD: GENERAL (Ubicación/Contacto) ---
            DataCard(
                title = "Ubicación y Contacto",
                onEditClick = { /* navController.navigate("editarContacto") */ }
            ) {
                // Organizado en 2x2
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InfoItem(label = "País", value = userData.pais, modifier = Modifier.weight(1f))
                        InfoItem(label = "Región", value = userData.region, modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InfoItem(label = "Ciudad", value = userData.ciudad, modifier = Modifier.weight(1f))
                        InfoItem(label = "Teléfono", value = userData.telefono, modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ----------------------------------------------------
// --- COMPONENTES REUTILIZABLES ---
// ----------------------------------------------------

/**
 * Muestra el área verde de la cabecera y la foto de perfil.
 */
@Composable
fun HeaderProfile(userData: UserData) {
    // El área verde que se ve en la maqueta
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp) // Altura para cubrir la mitad de la imagen
            .background(Color(0xFFE8F5E9).copy(alpha = 0.5f)), // Tonalidad verde muy clara
        contentAlignment = Alignment.Center
    ) {
        // Contenedor para la foto/placeholder centrado
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
            // Placeholder similar al de la maqueta
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant, // Fondo gris claro
                modifier = Modifier.size(120.dp),
                shadowElevation = 4.dp
            ) {
                Icon(
                    painter = rememberAsyncImagePainter("https://www.gstatic.com/images/icons/material/system/2x/photo_camera_grey600_48dp.png"), // Ícono de cámara
                    contentDescription = "Sin foto",
                    tint = Color.Gray,
                    modifier = Modifier.size(64.dp).padding(16.dp)
                )
            }
        }
    }
}

/**
 * Tarjeta de información con título, icono de edición y contenido personalizable.
 */
@Composable
fun DataCard(
    title: String,
    onEditClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Fondo blanco
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Título con Icono de Edición
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onEditClick) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar $title",
                        tint = MaterialTheme.colorScheme.primary // O un color de acento
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Contenido (los InfoItems)
            content()
        }
    }
}


/**
 * Muestra una etiqueta (label) y su valor (value), con un mejor espaciado.
 */
@Composable
fun InfoItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium, // Label más pequeño y menos prominente
            color = MaterialTheme.colorScheme.onSurfaceVariant // Gris claro
        )
        Text(
            text = if (value.isNotEmpty()) value else "Ejemplo", // Usamos "Ejemplo" si está vacío, como en la maqueta
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal // Valor de datos
        )
    }
}