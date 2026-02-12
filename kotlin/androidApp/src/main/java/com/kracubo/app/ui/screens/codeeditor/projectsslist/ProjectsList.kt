package com.kracubo.app.ui.screens.codeeditor.projectsslist

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
public fun ProjectsList(toCodeEditor: () -> Unit){
    Text("this is projects list")
    Button(onClick = {toCodeEditor()}) {
        Text("code editor")
    }
}