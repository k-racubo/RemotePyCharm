package com.kracubo.app.core.networking.handlers

import androidx.lifecycle.ViewModel
import com.kracubo.app.core.ViewModel.codeditor.ProjectsListViewModel
import com.kracubo.app.core.networking.Client
import core.ApiJson
import core.Response
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

        }
    }

    suspend fun getProjectsList() { Client.sendPacket(GetProjectsList(generateUuid())) }

    suspend fun openProject(projectName: String, projectPath: String) {
        Client.sendPacket(OpenProjectCommand(generateUuid(), projectName, projectPath))
    }

    private fun generateUuid(): String = UUID.randomUUID().toString()

    fun setCurrentViewmodel(viewmodel: ViewModel) { currentViewmodel = viewmodel }

    fun clearCurrentViewmodel() { currentViewmodel = null }
}