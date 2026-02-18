package com.kracubo.app.ui.screens.codeeditor.editor.components

import androidx.compose.ui.graphics.Color

object CodeColors {
    // Syntax highlighting colors (matching concept design)
    val Keyword = Color(0xFFFF6B6B) // Orange/red for keywords
    val Type = Color(0xFF87CEEB) // Light blue for functions/modules
    val Function = Color(0xFF87CEEB) // Light blue for functions
    val String = Color(0xFF90EE90) // Light green for strings
    val Comment = Color(0xFF94A3B8)
    val Number = Color(0xFF00CED1) // Cyan/light blue for numbers
    val Default = Color(0xFFFFFFFF) // White for variables/plain text
    val Background = Color(0xFF1E1E1E) // Very dark grey/black for editor background
    val LineNumberBackground = Color(0xFF252526) // Dark grey for line numbers background
    val LineNumberText = Color(0xFF858585) // Light grey for line numbers
    val TopBarBackground = Color(0xFF2B2B2B) // Dark grey for top bar (slightly lighter than editor)
    val BottomBarBackground = Color(0xFF2B2B2B) // Dark grey for bottom bar
    val ButtonBackground = Color(0xFF1E293B)
}