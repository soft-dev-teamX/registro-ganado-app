package com.softaprendizaje.ganado.ui.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.google.firebase.auth.FirebaseAuth
import com.softaprendizaje.ganado.ui.data.Animal
import com.softaprendizaje.ganado.ui.data.Finca

class GanadoViewModel : ViewModel() {

    // --- MÉTRICAS ---
    var totalCarne by mutableStateOf(0.0)
    var totalLeche by mutableStateOf(0.0)
    var animalesProducidos by mutableStateOf(0)
    var gananciaPeso by mutableStateOf(0.0)

    // --- USER ---
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
        ?: throw Exception("❌ No hay usuario autenticado")

    // --- FIREBASE (Realtime Database) ---
    private val database = FirebaseDatabase.getInstance()
        .getReference("usuarios")
        .child(uid)
        .child("animales")

    private val fincaRef = FirebaseDatabase.getInstance()
        .getReference("usuarios")
        .child(uid)
        .child("finca")

    // --- ESTADOS ---
    private var _listaAnimales by mutableStateOf(listOf<Animal>())
    val animales: List<Animal> get() = _listaAnimales

    var finca by mutableStateOf<Finca?>(null)
        private set

    var fincaCreada by mutableStateOf(false)
        private set

    // --- CÁLCULO AUTOMÁTICO ---
    val totalMachos by derivedStateOf { _listaAnimales.count { it.esMacho } }
    val totalHembras by derivedStateOf { _listaAnimales.count { !it.esMacho } }

    // --- INIT ---
    init {
        cargarFinca()
        escucharAnimales()
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
            .addOnSuccessListener {
                finca = nuevaFinca
                fincaCreada = true
            }
            .addOnFailureListener {
                println("❌ Error al crear finca: ${it.message}")
            }
    }

    fun cargarFinca() {
        fincaRef.get()
            .addOnSuccessListener { snapshot ->
                val fincaEncontrada = snapshot.getValue(Finca::class.java)
                finca = fincaEncontrada
                fincaCreada = fincaEncontrada != null
            }
            .addOnFailureListener {
                println("❌ Error al cargar finca: ${it.message}")
            }
    }

    // ======================
    // 🐄 ANIMALES
    // ======================
    fun agregarAnimal(animal: Animal) {
        database.child(animal.id).setValue(animal)
            .addOnFailureListener { e ->
                println("❌ Error al guardar animal: ${e.message}")
            }
    }

    fun eliminarAnimal(animal: Animal) {
        database.child(animal.id).removeValue()
            .addOnFailureListener { e ->
                println("❌ Error al eliminar animal: ${e.message}")
            }
    }

    // --- 🔥 ESCUCHA EN TIEMPO REAL ---
    fun escucharAnimales() {

        database.addValueEventListener(object : ValueEventListener {

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

    fun cargarDatosUsuario(userId: String) {

        val animalesRef = FirebaseDatabase.getInstance()
            .getReference("usuarios")
            .child(userId)
            .child("animales")

        animalesRef.get()
            .addOnSuccessListener { snapshot ->

                val lista = snapshot.children.mapNotNull { it.getValue(Animal::class.java) }
                _listaAnimales = lista   // ← 🔥 esto actualiza TODO automáticamente
            }
            .addOnFailureListener {
                println("❌ Error cargando datos: ${it.message}")
            }
    }

}
