package com.jurobil.materiapp.domain.model

import java.util.UUID

data class Asignatura(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String = "",
    val nota: Double = 0.0,
    val completada: Boolean = false,
    val observaciones: String = "",
    val numero: Int = 0
)