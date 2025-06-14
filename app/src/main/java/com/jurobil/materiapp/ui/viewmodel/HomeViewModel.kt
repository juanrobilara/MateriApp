package com.jurobil.materiapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.jurobil.materiapp.data.local.AsignaturaDao
import com.jurobil.materiapp.data.local.AsignaturaEntity
import com.jurobil.materiapp.data.local.CarreraDao
import com.jurobil.materiapp.data.local.CarreraEntity
import com.jurobil.materiapp.domain.model.Asignatura
import com.jurobil.materiapp.domain.model.Carrera
import com.jurobil.materiapp.domain.model.CarreraResumen
import com.jurobil.materiapp.domain.repository.RepositorioCarreras
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
    private val firestore: FirebaseFirestore,
    private val carreraDao: CarreraDao,
    private val asignaturaDao: AsignaturaDao,
    private val repositorio: RepositorioCarreras
) : ViewModel() {

    private val _greeting = MutableStateFlow("")
    val greeting: StateFlow<String> = _greeting.asStateFlow()

    private val _carreras = MutableStateFlow<List<Carrera>>(emptyList())
    val carreras: StateFlow<List<Carrera>> = _carreras

    private val _asignaturas = MutableStateFlow<List<Asignatura>>(emptyList())
    val asignaturas: StateFlow<List<Asignatura>> = _asignaturas

    private val _resumenCarreras = MutableStateFlow<Map<String, CarreraResumen>>(emptyMap())
    val resumenCarreras: StateFlow<Map<String, CarreraResumen>> = _resumenCarreras

    init {
        getCurrentUserGreeting()
        sincronizarFirestoreConRoom()
        sincronizarAsignaturasFirestoreConRoom()
        observarCarrerasLocales()
        observarAsignaturasYActualizarResumen()
    }

    private fun getCurrentUserGreeting() {
        val currentUser = auth.currentUser
        _greeting.value = currentUser?.displayName.toString()
    }

    private fun sincronizarYObservar() {
        viewModelScope.launch {
            try {
                repositorio.sincronizarCarreras()
                val locales = repositorio.getCarrerasLocal()
                _carreras.value = locales
            } catch (e: Exception) {
                // Si falla Firestore, aún puedes mostrar datos locales
                _carreras.value = repositorio.getCarrerasLocal()
            }
        }
    }



    private fun sincronizarFirestoreConRoom() {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("users").document(uid).collection("carreras")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null) return@addSnapshotListener

                viewModelScope.launch {
                    val carreras = snapshot.documents.mapNotNull { doc ->
                        val carrera = doc.toObject(Carrera::class.java)?.copy(id = doc.id)
                        carrera?.let {
                            CarreraEntity(
                                id = it.id,
                                nombre = it.nombre,
                                descripcion = it.descripcion,
                                cantidadAsignaturas = it.cantidadAsignaturas
                            )
                        }
                    }
                    carreraDao.insertAll(carreras)
                }
            }
    }

    private fun observarCarrerasLocales() {
        viewModelScope.launch {
            carreraDao.getAllFlow().collect { entidades ->
                _carreras.value = entidades.map {
                    Carrera(
                        id = it.id,
                        nombre = it.nombre,
                        descripcion = it.descripcion,
                        cantidadAsignaturas = it.cantidadAsignaturas
                    )
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
            .document()

        val carrera = Carrera(
            id = carreraRef.id,
            nombre = nombre,
            descripcion = descripcion,
            cantidadAsignaturas = cantidadAsignaturas
        )

        carreraRef.set(carrera).addOnSuccessListener {
            repeat(cantidadAsignaturas) { index ->
                val asignatura = Asignatura(
                    nombre = "Asignatura ${index + 1}",
                    numero = index + 1
                )
                carreraRef.collection("asignaturas")
                    .document(asignatura.id)
                    .set(asignatura)
            }

            // Re-sincroniza después de agregar
            sincronizarYObservar()
        }
    }




    fun deleteCarrera(id: String) {
        val uid = auth.currentUser?.uid ?: return

        // 1. Eliminar de Firestore
        firestore.collection("users").document(uid)
            .collection("carreras").document(id).delete()
            .addOnSuccessListener {
                viewModelScope.launch {
                    // 2. Eliminar localmente en Room
                    carreraDao.deleteById(id)
                    asignaturaDao.clearByCarreraId(id)

                    // 3. Re-sincronizar o volver a cargar si lo necesitas
                    sincronizarYObservar()
                }
            }
    }


    private fun sincronizarAsignaturasFirestoreConRoom() {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("users").document(uid).collection("carreras")
            .get()
            .addOnSuccessListener { snapshot ->
                snapshot.documents.forEach { doc ->
                    val carreraId = doc.id
                    firestore.collection("users").document(uid)
                        .collection("carreras").document(carreraId)
                        .collection("asignaturas")
                        .get()
                        .addOnSuccessListener { asignaturasSnapshot ->
                            viewModelScope.launch {
                                val asignaturas = asignaturasSnapshot.documents.mapNotNull { asignaturaDoc ->
                                    asignaturaDoc.toObject(Asignatura::class.java)?.copy(id = asignaturaDoc.id)
                                }

                                val entidades = asignaturas.map {
                                    AsignaturaEntity(
                                        id = it.id,
                                        carreraId = carreraId,
                                        nombre = it.nombre,
                                        numero = it.numero,
                                        nota = it.nota,
                                        completada = it.completada
                                    )
                                }

                                asignaturaDao.insertAll(entidades)
                            }
                        }
                }
            }
    }

    private fun observarAsignaturasYActualizarResumen() {
        viewModelScope.launch {
            asignaturaDao.getAllFlow().collect { entidades ->
                val resumenMap = entidades.groupBy { it.carreraId }.mapValues { (_, asignaturas) ->
                    val total = asignaturas.size
                    val completadas = asignaturas.count { it.completada }
                    val porcentaje = if (total > 0) (completadas * 100 / total) else 0
                    val promedio = asignaturas.filter { it.completada }.map { it.nota }.average()
                        .takeIf { it.isFinite() } ?: 0.0

                    CarreraResumen(porcentaje, promedio)
                }
                _resumenCarreras.value = resumenMap
            }
        }
    }


    fun getAsignaturas(carreraId: String) {
        viewModelScope.launch {
            _asignaturas.value = repositorio.getAsignaturasLocal(carreraId)
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