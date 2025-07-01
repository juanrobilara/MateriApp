package com.jurobil.materiapp.ui.screens.homeScreen.tabs.materiasEnCursoScreen.model

import com.jurobil.materiapp.domain.model.Carrera

data class MateriasUiState(
    val asignaturasEnCurso: List<AsignaturaNew> = emptyList(),
    val asignaturasFinalizadas: List<AsignaturaNew> = emptyList(),
    val asignaturasPendientes: List<AsignaturaNew> = emptyList(),
    val carrera: Carrera? = null
)


enum class EstadoAsignatura {
    PENDIENTE, EN_CURSO, FINALIZADA
}


data class AsignaturaNew(
    val nombre: String,
    val nota: Double?,
    val completada: Boolean,
    val observaciones: String,
    val numero: Int,
    val fechaInicio: String,
    val fechaFin: String,
    val estado: EstadoAsignatura
)