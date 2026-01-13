package com.kracubo.networking.localServer.handlers

import com.kracubo.core.project.ProjectManager
import core.API
import core.Response
import kotlinx.serialization.json.Json
import project.GetProjectsList
import project.ProjectsListResponse

class Handler {
    val json = Json {
        ignoreUnknownKeys = true
        classDiscriminator = "type"
    }

    fun resolve(message: String) : Response {
        return when (val apiMessage = json.decodeFromString(API.serializer(), message)) {
            is GetProjectsList -> {
                ProjectsListResponse(apiMessage.requestId, true,
                    ProjectManager.getProjects(),
                    ProjectManager.getCurrentProject())
            }
            else -> TODO()
        }
    }
}