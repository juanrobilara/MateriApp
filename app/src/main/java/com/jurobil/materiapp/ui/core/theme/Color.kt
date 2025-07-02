package com.jurobil.materiapp.ui.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val PrimaryColor = Color(0xFF4A90E2)
val BackgroundColor = Color(0xFFF2F4F7)
val CardColor = Color.White
val TextColor = Color.Black

val WarningYellow: Color
    @Composable get() = if (isSystemInDarkTheme()) Color(0xFFFFD54F) else Color(0xFFFFF9C4)

val CompleteGreen: Color
    @Composable get() = if (isSystemInDarkTheme()) Color(0xFF66BB6A) else Color(0xFFC8E6C9)

val ProgressOrange: Color
    @Composable get() = if (isSystemInDarkTheme()) Color(0xFFFFA726) else Color(0xFFFFE0B2)
