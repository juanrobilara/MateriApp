package com.jurobil.materiapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.jurobil.materiapp.domain.model.Asignatura
import com.jurobil.materiapp.domain.model.Carrera
import com.jurobil.materiapp.domain.model.CarreraResumen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _greeting = MutableStateFlow("")
    val greeting: StateFlow<String> = _greeting.asStateFlow()

    private val _carreras = MutableStateFlow<List<Carrera>>(emptyList())
    val carreras: StateFlow<List<Carrera>> = _carreras.asStateFlow()

    private val _asignaturas = MutableStateFlow<List<Asignatura>>(emptyList())
    val asignaturas: StateFlow<List<Asignatura>> = _asignaturas

    private val _resumenCarreras = MutableStateFlow<Map<String, CarreraResumen>>(emptyMap())
    val resumenCarreras: StateFlow<Map<String, CarreraResumen>> = _resumenCarreras

    init {
        getCurrentUserGreeting()
        listenCarrerasYResumenes()
    }

    private fun getCurrentUserGreeting() {
        val currentUser = auth.currentUser
        _greeting.value = currentUser?.displayName.toString()
    }

    private fun listenCarrerasYResumenes() {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("users").document(uid).collection("carreras")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null) return@addSnapshotListener
                val carreras = snapshot.documents.mapNotNull { it.toObject(Carrera::class.java)?.copy(id = it.id) }
                _carreras.value = carreras

                val resumenMap = mutableMapOf<String, CarreraResumen>()
                carreras.forEach { carrera ->
                    firestore.collection("users")
                        .document(uid)
                        .collection("carreras")
                        .document(carrera.id)
                        .collection("asignaturas")
                        .addSnapshotListener { asignaturasSnapshot, _ ->
                            val asignaturas = asignaturasSnapshot?.toObjects(Asignatura::class.java) ?: emptyList()
                            val total = asignaturas.size
                            val completadas = asignaturas.count { it.completada }
                            val porcentaje = if (total > 0) (completadas * 100 / total) else 0
                            val promedio = asignaturas.filter { it.completada }.map { it.nota }.average()
                                .takeIf { it.isFinite() } ?: 0.0

                            resumenMap[carrera.id] = CarreraResumen(porcentaje, promedio)
                            _resumenCarreras.value = resumenMap.toMap()
                        }
                }
            }
    }

    fun updateCarrera(carreraId: String, nombre: String, descripcion: String) {
        val uid = auth.currentUser?.uid ?: return
        val carreraRef = firestore.collection("users")
            .document(uid)
            .collection("carreras")
            .document(carreraId)

        carreraRef.update(
            mapOf(
                "nombre" to nombre,
                "descripcion" to descripcion
            )
        )
    }

    fun getCarrera(carreraId: String, onComplete: (Carrera?) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(uid)
            .collection("carreras")
            .document(carreraId)
            .get()
            .addOnSuccessListener { doc ->
                val carrera = doc.toObject(Carrera::class.java)?.copy(id = doc.id)
                onComplete(carrera)
            }
    }

    fun saveCarrera(nombre: String, descripcion: String, cantidadAsignaturas: Int) {
        val uid = auth.currentUser?.uid ?: return
        val carreraRef = firestore.collection("users")
            .document(uid)
            .collection("carreras")
            .document() // genera ID manual

        val carrera = Carrera(
            id = carreraRef.id,
            nombre = nombre,
            descripcion = descripcion,
            cantidadAsignaturas = cantidadAsignaturas
        )

        carreraRef.set(carrera)
            .addOnSuccessListener {
                repeat(cantidadAsignaturas) { index ->
                    val asignatura = Asignatura(
                        nombre = "Asignatura ${index + 1}",
                        numero = index + 1 // importante
                    )
                    carreraRef.collection("asignaturas")
                        .document(asignatura.id)
                        .set(asignatura)
                }
            }
    }


    fun deleteCarrera(id: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid).collection("carreras").document(id).delete()
    }


    fun getAsignaturas(carreraId: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(uid)
            .collection("carreras")
            .document(carreraId)
            .collection("asignaturas")
            .orderBy("numero") // <-- agrega esto
            .addSnapshotListener { snapshot, _ ->
                _asignaturas.value = snapshot?.documents?.mapNotNull {
                    it.toObject(Asignatura::class.java)?.copy(id = it.id)
                } ?: emptyList()
            }
    }

    fun updateAsignaturaCompletion(carreraId: String, asignaturaId: String, completada: Boolean) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(uid)
            .collection("carreras")
            .document(carreraId)
            .collection("asignaturas")
            .document(asignaturaId)
            .update("completada", completada)
    }

    fun signOut() {
        auth.signOut()

    }

    fun getUserProfile(onResult: (String?, String?) -> Unit) {
        val user = auth.currentUser
        onResult(user?.displayName, user?.photoUrl?.toString())
    }

    fun updateUserName(name: String, onResult: (Boolean) -> Unit) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        auth.currentUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
    }


}