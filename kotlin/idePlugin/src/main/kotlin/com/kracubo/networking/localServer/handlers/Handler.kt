package com.kracubo.networking.localServer.handlers

import com.kracubo.controlPanel.logger.Logger
import com.kracubo.controlPanel.logger.SenderType
import com.kracubo.core.project.ProjectManager
import core.API
import core.ApiJson
import core.Command
import core.Response
import project.GetProjectsList
import project.ProjectsListResponse

class Handler {
    fun resolve(message: String) : Response {
        return when (val apiMessage = ApiJson.instance.decodeFromString<Command>(message)) {
            is GetProjectsList -> {
                ProjectsListResponse(apiMessage.requestId, true,
                    ProjectManager.getProjects(),
                    ProjectManager.getCurrentProject())
            }
            else -> TODO()
        }
    }
}