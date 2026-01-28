package com.kracubo.core.project

import com.intellij.ide.RecentProjectsManager
import com.intellij.ide.RecentProjectsManagerBase
import com.intellij.openapi.project.ProjectManager
import com.kracubo.controlPanel.logger.Logger
import com.kracubo.controlPanel.logger.MessageType
import com.kracubo.controlPanel.logger.SenderType
import com.kracubo.core.session.SessionManager
import project.list.ProjectInfo

object ProjectManager {

    private var projectRunner: ProjectRunner? = null

    fun getProjects() : List<ProjectInfo>? {
        val manager = RecentProjectsManager.getInstance() as? RecentProjectsManagerBase
        if (manager == null) {
            Logger.log("RecentProjectsManager is not RecentProjectsManagerBase instance",
                SenderType.LOGGER, MessageType.ERROR)
            return null
        }

        val projects = mutableListOf<ProjectInfo>()

        manager.getRecentPaths().forEach { path ->
            projects.add(
                ProjectInfo(
                    manager.getProjectName(path),
                    path)
            )
        }

        return projects
    }

    fun getCurrentProject() : ProjectInfo {
        val openProjects = ProjectManager.getInstance().openProjects

        return ProjectInfo(openProjects.first().name, openProjects.first().basePath.toString())
    }

    fun openProject(projectName: String, projectPath: String) {
        if (SessionManager.setSession(projectPath)) {
            Logger.log("Project: $projectName is opened", SenderType.LOCAL_SERVER)
        } else
            Logger.log("Open project error: $projectName", SenderType.LOCAL_SERVER, MessageType.ERROR)
    }

    fun closeProject() {

    }

    fun runCurrentConfig() {
        if (projectRunner == null) { projectRunner = ProjectRunner() }
        projectRunner?.runCurrentConfig()
    }
}