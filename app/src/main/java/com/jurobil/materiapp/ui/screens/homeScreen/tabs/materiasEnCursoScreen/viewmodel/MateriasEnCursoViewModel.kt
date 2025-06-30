package com.jurobil.materiapp.ui.screens.homeScreen.tabs.materiasEnCursoScreen.viewmodel

import androidx.lifecycle.ViewModel
import com.jurobil.materiapp.domain.fakeRepository.FakeRepository
import com.jurobil.materiapp.domain.model.Carrera
import com.jurobil.materiapp.ui.screens.homeScreen.tabs.materiasEnCursoScreen.model.AsignaturaNew
import com.jurobil.materiapp.ui.screens.homeScreen.tabs.materiasEnCursoScreen.model.EstadoAsignatura
import com.jurobil.materiapp.ui.screens.homeScreen.tabs.materiasEnCursoScreen.model.MateriasUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MateriasEnCursoViewModel @Inject constructor(
    private val repository: FakeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MateriasUiState())
    val uiState: StateFlow<MateriasUiState> = _uiState.asStateFlow()

    private fun generateRandomDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, (-30..90).random())
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }

    private fun determineEstado(fechaInicio: String, fechaFin: String, nota: Double?): EstadoAsignatura {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = format.parse(format.format(Date()))!!
        val inicio = format.parse(fechaInicio)!!
        val fin = format.parse(fechaFin)!!

        return when {
            nota != null && nota >= 4.0 -> EstadoAsignatura.FINALIZADA
            today < inicio -> EstadoAsignatura.PENDIENTE
            today in inicio..fin -> EstadoAsignatura.EN_CURSO
            else -> EstadoAsignatura.FINALIZADA
        }
    }

    fun cargarAsignaturas(idCarrera: String) {
        val basicas = repository.generarAsignaturasPorCarrera(idCarrera)

        val completas = basicas.map { base ->
            val fechaInicio = generateRandomDate()
            val fechaFin = Calendar.getInstance().apply {
                time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(fechaInicio)!!
                add(Calendar.DAY_OF_YEAR, 30)
            }.let {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it.time)
            }

            val nota = if (base.completada) (4..10).random().toDouble() else null
            val estado = determineEstado(fechaInicio, fechaFin, nota)

            AsignaturaNew(
                nombre = base.nombre,
                nota = nota,
                completada = base.completada,
                observaciones = base.observaciones,
                numero = base.numero,
                fechaInicio = fechaInicio,
                fechaFin = fechaFin,
                estado = estado
            )
        }

        _uiState.value = MateriasUiState(
            asignaturasEnCurso = completas.filter { it.estado == EstadoAsignatura.EN_CURSO },
            asignaturasFinalizadas = completas.filter { it.estado == EstadoAsignatura.FINALIZADA },
            asignaturasPendientes = completas.filter { it.estado == EstadoAsignatura.PENDIENTE },
            carrera = repository.carrerasEjemplo.find { it.id == idCarrera }
                ?: repository.carrerasIntraconsulta.find { it.id == idCarrera }
        )
    }
}
