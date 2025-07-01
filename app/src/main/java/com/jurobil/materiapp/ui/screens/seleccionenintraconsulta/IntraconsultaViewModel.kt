package com.jurobil.materiapp.ui.screens.seleccionenintraconsulta

import androidx.lifecycle.ViewModel
import com.jurobil.materiapp.domain.fakeRepository.FakeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntraconsultaViewModel
@Inject constructor(
    private val fakeRepository: FakeRepository
) : ViewModel() {
    val carreras = fakeRepository.carrerasIntraconsulta

}