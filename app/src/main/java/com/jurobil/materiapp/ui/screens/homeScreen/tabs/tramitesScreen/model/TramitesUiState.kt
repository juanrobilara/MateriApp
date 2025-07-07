package com.jurobil.materiapp.ui.screens.homeScreen.tabs.tramitesScreen.model

data class TramitesUiState(
    val optionsMenu: OptionsMenu = OptionsMenu.MAIN_CONTENT_SCREEN
)


enum class OptionsMenu(val displayName: String) {
    MAIN_CONTENT_SCREEN("Trámites"),
    CONSTANCIA_SCREEN("Constancia"),
    INSCRIPCION_SCREEN("Inscripción"),
    VERIFICACION_SCREEN("Verificación"),
    CERTIFICADO_SCREEN("Certificado"),
    ANALITICO_SCREEN("Analítico"),
    BOLETO_SCREEN("Boleto estudiantil"),
    CALENDARIO_SCREEN("Calendario"),
    RECLAMO_SCREEN("Reclamo de notas"),
    PLANES_SCREEN("Planes de estudio"),
    CAMBIO_SCREEN("Cambio y simultaneidad"),
    FINALIZACION_SCREEN("Certificado de finalización"),
    MAS_ACCIONES_SCREEN("Más acciones")
}
