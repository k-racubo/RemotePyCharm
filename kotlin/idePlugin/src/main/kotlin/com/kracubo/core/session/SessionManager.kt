package com.kracubo.core.session

import com.intellij.ide.impl.OpenProjectTask
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.project.ex.ProjectManagerEx
import java.lang.ref.WeakReference
import java.nio.file.Paths

object SessionManager {

    private var projectRef: WeakReference<Project>? = null

    val currentProject: Project?
        get() = projectRef?.get()?.takeIf { !it.isDisposed }

    fun setSession(projectPath: String) : Boolean {
        val path = Paths.get(projectPath)

        val options = OpenProjectTask {
            runConfigurators = true
            forceOpenInNewFrame = true
            isNewProject = false
            projectName = null
            useDefaultProjectAsTemplate = false
            showWelcomeScreen = false // - skip open project alert
        }

        val newProject = ProjectManagerEx.getInstanceEx().openProject(path, options) ?: return false

        newProject.messageBus.connect().subscribe(ProjectManager.TOPIC, object : ProjectManagerListener {
            override fun projectClosed(project: Project) { if (project == currentProject) { projectRef = null } }
        })

        projectRef = WeakReference(newProject)

        return true
    }
}