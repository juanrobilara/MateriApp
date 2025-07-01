package com.jurobil.materiapp.ui.screens.homeScreen.tabs.tramitesScreen.viewmodel

import androidx.lifecycle.ViewModel
import com.jurobil.materiapp.ui.screens.homeScreen.tabs.tramitesScreen.model.OptionsMenu
import com.jurobil.materiapp.ui.screens.homeScreen.tabs.tramitesScreen.model.TramitesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TramitesViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(TramitesUiState())
    val uiState = _uiState.asStateFlow()


    fun changeOptionsMenu(option: OptionsMenu) {
        _uiState.update { currentState ->
            currentState.copy(
                optionsMenu = option
            )
        }
    }


}