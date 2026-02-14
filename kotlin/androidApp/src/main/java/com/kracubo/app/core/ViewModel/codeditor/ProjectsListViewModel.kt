package com.kracubo.app.core.ViewModel.codeditor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kracubo.app.core.networking.handlers.Handler
import kotlinx.coroutines.launch
import project.list.ProjectInfo

class ProjectsListViewModel : ViewModel() {

    init {
        Handler.setCurrentViewmodel(this)

        viewModelScope.launch {
            Handler.getProjectsList()
        }
    }

    fun updateProjectList(projects: List<ProjectInfo>?) {
        // нужна связка с ui и состояниями списка проекта
    }

    fun openProject(projectName: String, projectPath: String) {
        viewModelScope.launch {
            Handler.openProject(projectName, projectPath)
        }
    }

    override fun onCleared() { Handler.clearCurrentViewmodel() }
}