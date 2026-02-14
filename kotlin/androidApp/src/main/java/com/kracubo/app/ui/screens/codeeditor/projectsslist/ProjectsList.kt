package com.kracubo.app.ui.screens.codeeditor.projectsslist

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kracubo.app.core.ViewModel.codeditor.ProjectsListViewModel

@Composable
fun ProjectsList(toCodeEditor: () -> Unit) {
    val viewmodel: ProjectsListViewModel = viewModel()

    Text("this is projects list")
    Button(onClick = {toCodeEditor()}) {
        Text("code editor")
    }
}