package com.kracubo.core.project

import com.intellij.execution.ExecutionManager
import com.intellij.execution.RunManager
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.kracubo.controlPanel.logger.Logger
import com.kracubo.controlPanel.logger.MessageType
import com.kracubo.controlPanel.logger.SenderType
import com.kracubo.core.session.SessionManager

class ProjectRunner {

    fun runCurrentConfig() {
        val project = SessionManager.currentProject

        if (project == null) {
            Logger.log("Project session is closed or not initialized", SenderType.LOGGER, MessageType.ERROR)
            return
        }

        val runManager: RunManager = RunManager.getInstance(project)

        val selected = runManager.selectedConfiguration

        if (selected == null) {
            Logger.log("Setup run configuration for run project", SenderType.LOGGER, MessageType.WARNING)
            return
        }

        try {
            val executor = DefaultRunExecutor.getRunExecutorInstance()

            val environment = ExecutionEnvironmentBuilder.create(executor, selected).build()

            ExecutionManager.getInstance(project).restartRunProfile(environment)


        } catch (e: Exception) {
            Logger.log("Run Project error: $e", SenderType.LOGGER, MessageType.ERROR)
        }
    }
}