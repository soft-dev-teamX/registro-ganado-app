package com.softaprendizaje.ganado.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.google.firebase.auth.FirebaseAuth
import com.softaprendizaje.ganado.ui.data.Animal
import com.softaprendizaje.ganado.ui.data.Finca

// ⚠️ Nota: Asume que las clases Animal y Finca están correctamente definidas
// en com.softaprendizaje.ganado.ui.data

class GanadoViewModel : ViewModel() {

    // --- ESTADOS DE MÉTRICAS ---
    var totalCarne by mutableStateOf(0.0)
    var totalLeche by mutableStateOf(0.0)
    var animalesProducidos by mutableStateOf(0)
    var gananciaPeso by mutableStateOf(0.0)

    // --- ESTADOS DE USUARIO/FINCA ---
    var nombreUsuario by mutableStateOf("...") // Valor inicial mientras carga
        private set

    private var _listaAnimales by mutableStateOf(listOf<Animal>())
    val animales: List<Animal> get() = _listaAnimales

    var finca by mutableStateOf<Finca?>(null)
        private set

    // 🔑 ESTADO CLAVE: Determina si mostrar métricas o botón de crear finca
    var fincaCreada by mutableStateOf(false)
        private set

    // --- CÁLCULO AUTOMÁTICO ---
    val totalMachos by derivedStateOf { _listaAnimales.count { it.esMacho } }
    val totalHembras by derivedStateOf { _listaAnimales.count { !it.esMacho } }

    // --- USER & FIREBASE REFERENCES ---
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
        ?: throw Exception("❌ No hay usuario autenticado")

    private val database = FirebaseDatabase.getInstance()

    // 1. REFERENCIA A LA UBICACIÓN DE LOS ANIMALES
    private val animalesRef = database
        .getReference("usuarios")
        .child(uid)
        .child("animales")

    // 2. REFERENCIA A LA UBICACIÓN DE LA FINCA (RUTA CORRECTA)
    private val fincaRef = database
        .getReference("usuarios")
        .child(uid)
        .child("finca")

    // 3. REFERENCIA A LOS DATOS DEL USUARIO (PARA EL NOMBRE)
    private val usuarioRef = database
        .getReference("Usuarios") // Usamos "Usuarios" si así lo creaste en RegisterScreen
        .child(uid)


    // --- INIT ---
    init {
        // Ejecutamos las escuchas al inicio del ViewModel
        escucharAnimales()
        escucharFinca()
    }

    // ======================
    // 🚜 FINCA
    // ======================
    fun crearFinca(nombre: String, proposito: String, area: Double) {
        val nuevaFinca = Finca(
            nombre = nombre,
            proposito = proposito,
            area = area
        )

        fincaRef.setValue(nuevaFinca)
            .addOnFailureListener {
                println("❌ Error al crear finca: ${it.message}")
            }
    }

    /** ESCUCHA CONSTANTE para la Finca (SOLUCIÓN AL BUG) */
    private fun escucharFinca() {
        fincaRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fincaEncontrada = snapshot.getValue(Finca::class.java)
                finca = fincaEncontrada
                // 🔑 CLAVE: Si la snapshot existe (no es nula), la finca está creada.
                fincaCreada = fincaEncontrada != null
            }

            override fun onCancelled(error: DatabaseError) {
                println("❌ Error al escuchar finca: ${error.message}")
            }
        })
    }

    // ======================
    // 🐄 ANIMALES
    // ======================
    fun agregarAnimal(animal: Animal) {
        animalesRef.child(animal.id).setValue(animal)
            .addOnFailureListener { e ->
                println("❌ Error al guardar animal: ${e.message}")
            }
    }

    fun eliminarAnimal(animal: Animal) {
        animalesRef.child(animal.id).removeValue()
            .addOnFailureListener { e ->
                println("❌ Error al eliminar animal: ${e.message}")
            }
    }

    // --- 🔥 ESCUCHA EN TIEMPO REAL de Animales ---
    private fun escucharAnimales() {

        animalesRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val lista = snapshot.children.mapNotNull { it.getValue(Animal::class.java) }
                _listaAnimales = lista

                // --- CALCULAR MÉTRICAS ---
                totalCarne = lista.sumOf { it.carne }
                totalLeche = lista.sumOf { it.leche }
                gananciaPeso = lista.sumOf { it.gananciaPeso }
                animalesProducidos = lista.count { it.producido }
            }

            override fun onCancelled(error: DatabaseError) {
                println("❌ Error Firebase: ${error.message}")
            }
        })
    }

    // --- FUNCIÓN PARA CARGAR DATOS DEL USUARIO (SOLO NOMBRE) ---
    fun cargarDatosUsuario(userId: String) {

        usuarioRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nombreDesdeFirebase = snapshot.child("nombre").getValue(String::class.java)
                if (nombreDesdeFirebase != null) {
                    nombreUsuario = nombreDesdeFirebase
                } else {
                    val emailUsuario = FirebaseAuth.getInstance().currentUser?.email
                    nombreUsuario = emailUsuario ?: "Usuario"
                }
            }
            override fun onCancelled(error: DatabaseError) {
                println("❌ Error al cargar nombre de usuario: ${error.message}")
                nombreUsuario = "Usuario"
            }
        })
    }
}
