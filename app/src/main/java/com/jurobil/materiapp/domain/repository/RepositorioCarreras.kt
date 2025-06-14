package com.jurobil.materiapp.domain.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jurobil.materiapp.data.local.AsignaturaDao
import com.jurobil.materiapp.data.local.AsignaturaEntity
import com.jurobil.materiapp.data.local.CarreraDao
import com.jurobil.materiapp.data.local.CarreraEntity
import com.jurobil.materiapp.domain.model.Asignatura
import com.jurobil.materiapp.domain.model.Carrera
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositorioCarreras @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val carreraDao: CarreraDao,
    private val asignaturaDao: AsignaturaDao
) {

    suspend fun sincronizarCarreras() {
        val uid = auth.currentUser?.uid ?: return
        val snapshot = firestore.collection("users").document(uid).collection("carreras").get().await()

        val carreras = snapshot.documents.mapNotNull { doc ->
            doc.toObject(Carrera::class.java)?.copy(id = doc.id)
        }

        val carrerasEntity = carreras.map {
            CarreraEntity(it.id, it.nombre, it.descripcion, it.cantidadAsignaturas)
        }

        carreraDao.clearAll()
        carreraDao.insertAll(carrerasEntity)

        carreras.forEach { carrera ->
            val asignaturasSnap = firestore.collection("users")
                .document(uid)
                .collection("carreras")
                .document(carrera.id)
                .collection("asignaturas")
                .get().await()

            val asignaturas = asignaturasSnap.documents.mapNotNull { doc ->
                doc.toObject(Asignatura::class.java)?.copy(id = doc.id)
            }

            asignaturaDao.clearByCarreraId(carrera.id)

            val asignaturasEntity = asignaturas.map {
                AsignaturaEntity(it.id, carrera.id, it.nombre, it.nota, it.completada, it.numero)
            }

            asignaturaDao.insertAll(asignaturasEntity)
        }
    }

    suspend fun getCarrerasLocal(): List<Carrera> {
        return carreraDao.getAllFlow().first()
            .map {
            Carrera(it.id, it.nombre, it.descripcion, it.cantidadAsignaturas)
        }
    }

    suspend fun getAsignaturasLocal(carreraId: String): List<Asignatura> {
        return asignaturaDao.getByCarreraId(carreraId).map {
            Asignatura(
                id = it.id,
                nombre = it.nombre,
                nota = it.nota,
                completada = it.completada,
                numero = it.numero
            )
        }
    }
}