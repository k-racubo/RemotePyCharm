package com.kracubo.networking.localServer.handlers

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.components.service
import com.kracubo.core.file.FileManager
import com.kracubo.core.project.CoreProjectManager
import com.kracubo.core.project.ProjectRunner
import com.kracubo.core.project.ProjectStructureProvider
import core.ApiJson
import core.Command
import core.ErrorResponse
import core.Response
import file.FileContentResponse
import file.GetFileContent
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import project.list.GetProjectsList
import project.open.OpenProjectCommand
import project.open.ProjectFileTreeResponse
import project.list.ProjectsListResponse
import project.run.ResultOfRunResponse
import project.run.RunCurrentConfigCommand

class Handler {

    suspend fun resolve(message: String): Response {
        val projectManager = CoreProjectManager.getInstance()

        return try {
            when (val apiMessage = ApiJson.instance.decodeFromString<Command>(message)) {
                is GetProjectsList -> {
                    ProjectsListResponse(
                        apiMessage.requestId,
                        true,
                        projectManager.getProjects(),
                        projectManager.getCurrentProjectInfo()
                    )
                }
                is OpenProjectCommand -> {
                    projectManager.openProject(apiMessage.projectName, apiMessage.projectPath)

                    projectManager.runWithProject(
                        action = { project ->
                            val tree = runReadAction { project.service<ProjectStructureProvider>().buildTree() }

                            ProjectFileTreeResponse(
                                requestId = apiMessage.requestId,
                                success = true,
                                fileTree = tree
                            )
                        },
                        onError = {
                            ErrorResponse(
                                apiMessage.requestId,
                                false,
                                "OPEN_FAILED",
                                "Project not found after opening"
                            )
                        })
                }
                is RunCurrentConfigCommand -> {
                    projectManager.runWithProject(
                        action = {project ->
                            val result = project.service<ProjectRunner>().runCurrentConfigAsync()

                            ResultOfRunResponse(
                                apiMessage.requestId,
                                true,
                                result
                            )
                        },
                        onError = {
                            ErrorResponse(
                                apiMessage.requestId,
                                false,
                                "RUN_FAILED",
                                "Open project before"
                            )
                        }
                    )
                }
                is GetFileContent -> {
                    projectManager.runWithProject(
                        action = { project ->
                            val content = project.service<FileManager>().getFileContent(apiMessage.filePath)

                            if (content.isEmpty()) {
                                return@runWithProject ErrorResponse(
                                    apiMessage.requestId,
                                    false,
                                    "GET_FILE_CONTENT_FAILED",
                                    "VFS can't read or found file"
                                    )
                            }

                            FileContentResponse(apiMessage.requestId, true, content)
                        },
                        onError = {
                            ErrorResponse(
                                apiMessage.requestId,
                                false,
                                "GET_FILE_CONTENT_FAILED",
                                "Project not found for get file content"
                            )
                        }
                    )
                }
                else -> {
                    ErrorResponse(
                        requestId = "unknown",
                        success = false,
                        errorCode = "UNKNOWN_COMMAND",
                        errorMessage = "Command parsed but not handled in when block"
                    ) // компилятор только не бей лучше схавай это, но не бей :(
                }
            }

        } catch (e: Exception) {
            val requestId = try {
                ApiJson.instance.parseToJsonElement(message)
                    .jsonObject["requestId"]?.jsonPrimitive?.content
                    ?: "unknown"
            } catch (_: Exception) { "unknown" }

            ErrorResponse(
                requestId,
                false,
                "RESOLVE_ERROR",
                "Handler error: ${e.message}"
            )
        }
    }
}