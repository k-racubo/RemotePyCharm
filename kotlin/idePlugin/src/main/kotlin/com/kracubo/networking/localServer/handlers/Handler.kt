package com.kracubo.networking.localServer.handlers

import com.kracubo.core.project.ProjectManager
import core.ApiJson
import core.Command
import core.ErrorResponse
import core.Response
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import project.list.GetProjectsList
import project.open.OpenProjectCommand
import project.open.ProjectFileTreeResponse
import project.list.ProjectsListResponse
import project.run.ResultOfRunResponse
import project.run.RunCurrentConfigCommand

class Handler {

    fun resolve(message: String): Response {
        return try {
            when (val apiMessage = ApiJson.instance.decodeFromString<Command>(message)) {
                is GetProjectsList -> {
                    ProjectsListResponse(
                        apiMessage.requestId,
                        true,
                        ProjectManager.getProjects(),
                        ProjectManager.getCurrentProject()
                    )
                }
                is OpenProjectCommand -> {
                    ProjectManager.openProject(apiMessage.projectName, apiMessage.projectPath)
                    ProjectFileTreeResponse(
                        apiMessage.requestId,
                        true,
                        mapOf("67" to "67")
                    ) // just затычки
                }
                is RunCurrentConfigCommand -> {
                    ProjectManager.runCurrentConfig()
                    ResultOfRunResponse(
                        apiMessage.requestId,
                        true,
                        "ugu"
                    ) // just затычки
                }
                else -> {
                    ErrorResponse(
                        apiMessage.requestId,
                        false,
                        "1234",
                        "Wtf with yr json go fuck man "
                    ) // как может выполниться это условие?
                }
            }

        } catch (e: Exception) {
            val requestId = try {
                ApiJson.instance.parseToJsonElement(message)
                    .jsonObject["requestId"]?.jsonPrimitive?.content
                    ?: "unknown"
            } catch (_: Exception) {
                "unknown"
            }

            ErrorResponse(
                requestId,
                false,
                "RESOLVE_ERROR",
                "Handler error: ${e.message}"
            )
        }
    }
}