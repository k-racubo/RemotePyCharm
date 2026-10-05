package com.kracubo.networking.localServer.handlers.commands

import com.google.auto.service.AutoService
import com.kracubo.networking.localServer.handlers.ICommandHandler
import core.Response
import core.GetProjectsListCommand
import core.ProjectsListResponse

@Suppress("UNUSED")
@AutoService(ICommandHandler::class)
class GetProjectsListHandler : ICommandHandler<GetProjectsListCommand> {
    override val commandClass = GetProjectsListCommand::class

    override suspend fun handle(command: GetProjectsListCommand): Response {
        return ProjectsListResponse(
            command.requestId,
            projectManager.getProjects(),
            projectManager.getCurrentProjectInfo()
        )
    }
}