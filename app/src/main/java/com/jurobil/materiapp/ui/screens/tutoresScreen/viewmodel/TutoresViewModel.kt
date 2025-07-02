package com.jurobil.materiapp.ui.screens.tutoresScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jurobil.materiapp.domain.fakeRepository.FakeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TutoresViewModel @Inject constructor(
    private val fakeRepository: FakeRepository
): ViewModel() {

    private val _query = MutableStateFlow("")
    private val _materiaFiltro = MutableStateFlow<String?>(null)

    val query = _query.asStateFlow()
    val materiaFiltro = _materiaFiltro.asStateFlow()

    val tutoresFiltrados = combine(_query, _materiaFiltro) { query, filtro ->
        fakeRepository.filtrarTutores(query, filtro)
    }.stateIn(viewModelScope, SharingStarted.Lazily, fakeRepository.tutoresEjemplo)

    fun actualizarQuery(nuevaQuery: String) {
        _query.value = nuevaQuery
    }

    fun actualizarFiltroMateria(materia: String?) {
        _materiaFiltro.value = materia
    }
}