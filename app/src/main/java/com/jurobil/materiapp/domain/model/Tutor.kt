package com.jurobil.materiapp.domain.model

data class Tutor(
    val id: String,
    val nombre: String,
    val areaExpertise: String,
    val materias: List<String>,
    val fotoUrl: String? = null
)