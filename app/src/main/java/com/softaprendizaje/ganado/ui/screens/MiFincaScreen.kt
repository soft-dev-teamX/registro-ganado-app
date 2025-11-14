package com.softaprendizaje.ganado.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.softaprendizaje.ganado.R // **Asegúrate de tener esta referencia a tus recursos**
import com.softaprendizaje.ganado.ui.viewmodels.GanadoViewModel

import androidx.compose.material3.TopAppBarDefaults
import com.softaprendizaje.ganado.ui.theme.main_blue

// **Nota Importante:** Necesitas una clase de datos 'Finca' en tu proyecto que contenga:
// nombre, proposito, areaTotal, areaGanado, ubicacion (o propiedades equivalentes).

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiFincaScreen(
    onNavigateBack: () -> Unit,
    viewModel: GanadoViewModel = viewModel()) {

    // 🐄 Usando el objeto 'finca' real del ViewModel
    val finca = viewModel.finca

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finca") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // Solo mostramos Editar si la finca existe
                    if (finca != null) {
                        IconButton(onClick = { /* Lógica de Edición */ }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar")
                        }
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    // Aquí usamos la variable importada 'main_blue'
                    containerColor = main_blue,

                    // Los colores de contenido (letras/íconos) deben ser
                    // un color que contraste con main_blue (como el blanco)
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Lógica original: Si la finca es null, muestra un mensaje.
            if (finca == null) {
                Text(
                    "No has registrado ninguna finca todavía.",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                // Si la finca existe, mostramos la interfaz detallada de la maqueta

                // 1. Imagen de Cabecera (Vaca)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Image(
                        // Reemplaza 'R.drawable.cow_placeholder' con tu recurso real
                        painter = painterResource(id = R.drawable.cow_placeholder),
                        contentDescription = "Imagen de la finca",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Contenedor principal para las tarjetas (se superpone a la imagen)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-30).dp)
                ) {
                    // 2. Tarjeta de Nombre (La rosa)
                    FincaDetailCard(
                        content = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Pequeña imagen/icono a la izquierda
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.LightGray)
                                ) { /* Icono de granja */ }
                                Spacer(Modifier.width(16.dp))
                                Text(
                                    text = finca.nombre,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    )

                    // 3. Tarjeta de Propósito (Doble Propósito)
                    FincaDetailCard(
                        content = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Simulación del icono de propósito
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF9E8D0))
                                ) {
                                    // Utiliza finca.proposito
                                    Text("🥩🥛", modifier = Modifier.align(Alignment.Center))
                                }
                                Spacer(Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = "Propósito",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = finca.proposito,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    )

                    // 4. Tarjeta de Áreas (Total y Ganado)
                    FincaDetailCard(
                        content = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                // Área Total - Asegúrate que 'finca' tenga una propiedad para el área total (ej: areaTotal)
                                AreaDetailColumn(
                                    label = "Área total",
                                    value = "${finca.area} ha", // **Asume propiedad 'areaTotal'**
                                    iconPainter = painterResource(id = R.drawable.area_icon)
                                )
                                Spacer(Modifier.width(16.dp))
                                Divider(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .width(1.dp)
                                )
                                Spacer(Modifier.width(16.dp))
                                // Área de Ganado - Asume propiedad 'areaGanado'
                                AreaDetailColumn(
                                    label = "Área de ganado",
                                    value = "${finca.areaGanado} ha", // **Asume propiedad 'areaGanado'**
                                    iconPainter = painterResource(id = R.drawable.ic_pets)
                                )
                            }
                        }
                    )

                    // 5. Tarjeta de Ubicación
                    FincaDetailCard(
                        content = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.LocationOn,
                                    contentDescription = "Ubicación",
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Spacer(Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = "Ubicación",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    // Asume propiedad 'ubicacion' que concatena el país, departamento y ciudad
                                    Text(
                                        text = finca.ubicacion, // **Asume propiedad 'ubicacion'**
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    )

                    // 6. Botón de Eliminar Finca
                    Spacer(Modifier.height(32.dp))
                    Button(
                        onClick = { /* Lógica de Eliminar Finca */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(56.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Text("Eliminar finca", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

// --- Componentes Reutilizables (sin cambios, puedes copiarlos directamente) ---

@Composable
fun FincaDetailCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
fun AreaDetailColumn(label: String, value: String, iconPainter: androidx.compose.ui.graphics.painter.Painter) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = iconPainter,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}