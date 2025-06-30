package com.jurobil.materiapp.ui.screens.homeScreen.tabs.tramitesScreen.model

data class TramitesUiState(
    val optionsMenu: OptionsMenu = OptionsMenu.MAIN_CONTENT_SCREEN
)


enum class OptionsMenu(name: String) {
    MAIN_CONTENT_SCREEN( "Tramites"),
    CONSTANCIA_SCREEN("Constania"),
    INSCRIPCION_SCREEN("Inscripción"),
    VERIFICACION_SCREEN( "Verificación"),
    CERTIFICADO_SCREEN("Certificado"),
    ANALITICO_SCREEN("Analítico"),
    BOLETO_SCREEN("Boleto estudiantil"),
    CALENDARIO_SCREEN("Calendario"),
    MAS_ACCIONES_SCREEN("Mas acciones")
}

