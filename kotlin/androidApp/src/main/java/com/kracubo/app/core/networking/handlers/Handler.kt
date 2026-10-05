package com.kracubo.app.core.networking.handlers

import androidx.lifecycle.ViewModel
import com.kracubo.app.core.viewmodels.codeditor.ProjectsListViewModel
import com.kracubo.app.core.networking.Client
import com.kracubo.app.core.viewmodels.codeditor.BaseViewModel
import com.kracubo.app.core.viewmodels.codeditor.CodeEditorViewModel
import core.ApiJson
import core.Event
import core.Response
import core.FileContentResponse
import core.GetFileContentCommand
import core.OnProjectClosedEvent
import core.CloseProjectCommand
import core.GetProjectsListCommand
import core.ProjectsListResponse
import core.OpenProjectCommand
import core.ProjectFileTreeResponse
import core.ResultOfRunResponse
import core.RunCurrentConfigCommand
import core.StopCurrentConfigCommand
import java.util.UUID

object Handler {
    private var currentViewmodel: ViewModel? = null

    fun resolve(message: String) {
        try {
            when (val apiMessage = ApiJson.instance.decodeFromString<Response>(message)) {
                is ProjectsListResponse -> { (currentViewmodel as? ProjectsListViewModel)?.updateProjectList(apiMessage.projects) }
                is ProjectFileTreeResponse -> {
                    (currentViewmodel as? CodeEditorViewModel)?.updateProjectTree(apiMessage.fileTree)
                }
                is ResultOfRunResponse -> {
                    (currentViewmodel as? CodeEditorViewModel)?.onRunResult(apiMessage.result)
                }
                is FileContentResponse -> {
                    (currentViewmodel as? CodeEditorViewModel)?.updateCurrentFileContent(apiMessage.content)
                }
                else -> {}
            }
        } catch (_: Exception) {
            when (ApiJson.instance.decodeFromString<Event>(message)) {
                is OnProjectClosedEvent -> {
                    (currentViewmodel as? CodeEditorViewModel)?.onProjectClosedOnServer()
                }

                else -> {}
            }
        }
    }

    suspend fun getProjectsList() { Client.sendPacket(GetProjectsListCommand(generateUuid())) }

    suspend fun openProject(projectName: String, projectPath: String) {
        Client.sendPacket(OpenProjectCommand(generateUuid(), projectName, projectPath))
    }

    suspend fun closeCurrentProject() {
        Client.sendPacket(CloseProjectCommand(generateUuid()))
    }

    private fun generateUuid(): String = UUID.randomUUID().toString()

    fun onDisconnect() { (currentViewmodel as? BaseViewModel)?.onDisconnect() }

    suspend fun runProject() { Client.sendPacket(RunCurrentConfigCommand(generateUuid())) }

    suspend fun stopProject() { Client.sendPacket(StopCurrentConfigCommand(generateUuid())) }

    suspend fun getFileContent(filePath: String) {
        Client.sendPacket(GetFileContentCommand(generateUuid(), filePath))
    }

    fun setCurrentViewmodel(viewmodel: ViewModel) { currentViewmodel = viewmodel }

    fun clearCurrentViewmodel() { currentViewmodel = null }
}