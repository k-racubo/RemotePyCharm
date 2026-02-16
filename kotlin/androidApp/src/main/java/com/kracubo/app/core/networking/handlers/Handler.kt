package com.kracubo.app.core.networking.handlers

import androidx.lifecycle.ViewModel
import com.kracubo.app.core.viewmodels.codeditor.ProjectsListViewModel
import com.kracubo.app.core.networking.Client
import com.kracubo.app.core.viewmodels.codeditor.BaseViewModel
import com.kracubo.app.core.viewmodels.codeditor.CodeEditorViewModel
import core.ApiJson
import core.Event
import core.Response
import project.OnProjectClosed
import project.close.CloseProjectCommand
import project.list.GetProjectsList
import project.list.ProjectsListResponse
import project.open.OpenProjectCommand
import java.util.UUID

object Handler {
    private var currentViewmodel: ViewModel? = null

    fun resolve(message: String) {
        try {
            when (val apiMessage = ApiJson.instance.decodeFromString<Response>(message)) {
                is ProjectsListResponse -> { (currentViewmodel as? ProjectsListViewModel)?.updateProjectList(apiMessage.projects) }
                else -> {}
            }
        } catch (_: Exception) {
            when (ApiJson.instance.decodeFromString<Event>(message)) {
                is OnProjectClosed -> {
                    (currentViewmodel as? CodeEditorViewModel)?.onProjectClosedOnServer()
                }
            }
        }
    }

    suspend fun getProjectsList() { Client.sendPacket(GetProjectsList(generateUuid())) }

    suspend fun openProject(projectName: String, projectPath: String) {
        Client.sendPacket(OpenProjectCommand(generateUuid(), projectName, projectPath))
    }

    suspend fun closeCurrentProject() {
        Client.sendPacket(CloseProjectCommand(generateUuid()))
    }

    private fun generateUuid(): String = UUID.randomUUID().toString()

    fun onDisconnect() { (currentViewmodel as? BaseViewModel)?.onDisconnect() }

    fun setCurrentViewmodel(viewmodel: ViewModel) { currentViewmodel = viewmodel }

    fun clearCurrentViewmodel() { currentViewmodel = null }
}