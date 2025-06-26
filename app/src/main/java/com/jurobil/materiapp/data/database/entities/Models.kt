package com.jurobil.materiapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carreras")
data class CarreraEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val descripcion: String,
    val cantidadAsignaturas: Int
)

@Entity(tableName = "asignaturas")
data class AsignaturaEntity(
    @PrimaryKey val id: String,
    val carreraId: String,
    val nombre: String,
    val nota: Double,
    val completada: Boolean,
    val numero: Int
)