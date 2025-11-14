package com.softaprendizaje.ganado.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.softaprendizaje.ganado.ui.data.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MiCuentaViewModel : ViewModel() {

    // 1. MutableStateFlow para mantener el estado de los datos del usuario
    private val _userData = MutableStateFlow(UserData())
    val userData: StateFlow<UserData> = _userData

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    // Asegúrate de que este nodo coincida exactamente con la capitalización en Firebase
    private val dbRef = FirebaseDatabase.getInstance().getReference("Usuarios")

    init {
        // Iniciar la carga de datos tan pronto como el ViewModel se cree
        loadUserData()
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            // El usuario no está autenticado, no hay datos que cargar
            return
        }

        // 2. ÚNICO Listener para obtener datos en tiempo real de Firebase
        dbRef.child(userId).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // ❗️ LOG DE DIAGNÓSTICO: Muestra qué datos se reciben de Firebase (null o el mapa de datos)
                println("FIREBASE_DATA_RAW: ${snapshot.value}")

                val firebaseData = snapshot.getValue(UserData::class.java)

                if (firebaseData != null) {
                    // ❗️ LOG DE ÉXITO: Confirma que el mapeo fue exitoso
                    println("FIREBASE_DATA_MAPPED: ${firebaseData.nombre} ${firebaseData.apellido}")

                    viewModelScope.launch {
                        _userData.value = firebaseData
                    }
                } else {
                    // Log de error de mapeo (si los datos llegan pero no coinciden con UserData)
                    println("Error: No se pudieron mapear los datos del usuario. UID: $userId")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores de lectura de base de datos
                println("Error de Base de Datos: ${error.message}")
            }
        })
    }
}