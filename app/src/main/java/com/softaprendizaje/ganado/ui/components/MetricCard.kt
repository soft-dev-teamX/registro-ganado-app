package com.softaprendizaje.ganado.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Una tarjeta reutilizable para mostrar una métrica clave del dashboard.
 *
 * @param title El título de la tarjeta (ej. "Machos", "Total de carne").
 * @param value El valor principal que se muestra en grande (ej. "2", "2222.0 kg").
 * @param icon Un Composable que representa el icono de la tarjeta.
 * @param modifier Modificadores estándar de Compose.
 * @param subtitle Un texto opcional más pequeño que va debajo del valor (ej. "--").
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetricCard(
    title: String,
    value: String,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    Card(modifier = modifier, onClick = { /* Podrías navegar a un detalle */ }) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp) // Espacio entre elementos
        ) {
            // 1. Icono
            icon()

            // 2. Título
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center // Para títulos largos
            )

            // 3. Valor principal
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            // 4. Subtítulo (opcional)
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}