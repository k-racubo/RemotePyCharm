package com.kracubo.networking.localServer.handlers.commands

import com.google.auto.service.AutoService
import com.intellij.openapi.components.service
import com.kracubo.core.project.ProjectRunner
import com.kracubo.networking.localServer.handlers.ICommandHandler
import core.Response
import core.ResultOfRunResponse
import core.RunCurrentConfigCommand

@Suppress("UNUSED")
@AutoService(ICommandHandler::class)
class RunCurrentConfigCommandHandler : ICommandHandler<RunCurrentConfigCommand> {
    override val commandClass = RunCurrentConfigCommand::class

    override suspend fun handle(command: RunCurrentConfigCommand): Response? {
        return projectManager.runWithProject(
            action = {project ->
                val result = project.service<ProjectRunner>().runCurrentConfigAsync()

                ResultOfRunResponse(
                    command.requestId,
                    result
                )
            })
    }

}