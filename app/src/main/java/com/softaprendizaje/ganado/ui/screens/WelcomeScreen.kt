package com.softaprendizaje.ganado.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softaprendizaje.ganado.R

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6366F1)), // azul tipo mockup
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        // Título principal
        Text(
            text = "Bienvenido a Control Ganadero",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        // Icono central (vaca)
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.White, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_egg),
                contentDescription = "Logo principal",
                modifier = Modifier.size(70.dp),
                contentScale = ContentScale.Fit
            )
        }

        // Iconos de leche, carne, salud
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_milk), contentDescription = "Leche", modifier = Modifier.size(40.dp))
            Image(painter = painterResource(id = R.drawable.ic_pets), contentDescription = "Carne", modifier = Modifier.size(40.dp))
            Image(painter = painterResource(id = R.drawable.ic_health), contentDescription = "Salud", modifier = Modifier.size(40.dp))
        }

        // Descripción
        Text(
            text = "Podrá realizar informes individualizados y es la mejor forma de administrar su finca ganadera.",
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        )

        // Botones
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("INICIAR SESIÓN", color = Color(0xFF6366F1))
            }
            Button(
                onClick = onRegisterClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("REGISTRARSE", color = Color(0xFF6366F1))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
