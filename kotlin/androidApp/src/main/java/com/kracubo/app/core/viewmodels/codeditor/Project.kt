package com.kracubo.app.core.viewmodels.codeditor

import androidx.compose.ui.graphics.Color

data class Project(
    val initials: String,
    val name: String,
    val path: String,
    val color: Color
)