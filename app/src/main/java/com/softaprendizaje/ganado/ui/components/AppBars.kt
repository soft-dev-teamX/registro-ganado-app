package com.softaprendizaje.ganado.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home // Placeholder para "Inicio"
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Pets // Placeholder para "Animales"
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Grass // Placeholder para "Producción"
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning // Placeholder para "Alertas"
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.softaprendizaje.ganado.R // ⚠️ IMPORTANTE: Asumiendo que tus iconos están en res/drawable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


// Data class para manejar los ítems de navegación
data class BottomNavItem(
    val label: String,
    val icon: ImageVector, // Icono por defecto de Material
    // val customIcon: Int, // Descomenta esto para usar tus iconos de drawable
    val route: String
)

// --- BARRA SUPERIOR (TOP BAR) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FincaTopBar(onMenuClick: () -> Unit, onSearchClick: () -> Unit) {
    TopAppBar(
        title = { Text("Inicio") },
        navigationIcon = {
            IconButton(onClick = { onMenuClick() }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = { onSearchClick() }) {
                Icon(Icons.Default.Search, contentDescription = "Buscar")
            }
        }
    )
}


// --- BARRA INFERIOR (BOTTOM BAR) ---
@Composable
fun FincaBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    // Definimos los 4 ítems de la maqueta
    val navItems = listOf(
        BottomNavItem("Inicio", Icons.Default.Home, "inicio"),
        BottomNavItem("Animales", Icons.Default.Pets, "animales"),
        BottomNavItem("Reportes", Icons.Default.Grass, "reportes"),
                BottomNavItem("Alertas", Icons.Default.Warning, "alertas")
    )

    NavigationBar {
        navItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = {
                    // TODO: Aquí deberías usar tus iconos personalizados
                    // Ejemplo:
                    // Icon(
                    //   painter = painterResource(id = R.drawable.ic_finca),
                    //   contentDescription = item.label
                    // )

                    // Por ahora usamos los iconos de placeholder:
                    Icon(item.icon, contentDescription = item.label)
                },
                label = { Text(item.label) }
            )
        }
    }
}