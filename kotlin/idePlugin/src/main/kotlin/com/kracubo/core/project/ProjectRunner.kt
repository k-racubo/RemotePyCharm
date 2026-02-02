package com.kracubo.core.project

import com.intellij.execution.ExecutionManager
import com.intellij.execution.RunManager
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.kracubo.controlPanel.logger.Logger
import com.kracubo.controlPanel.logger.MessageType
import com.kracubo.controlPanel.logger.SenderType
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@Service(Service.Level.PROJECT)
class ProjectRunner(val project: Project) {

    suspend fun runCurrentConfigAsync(): String = suspendCancellableCoroutine { continuation ->
        runCurrentConfig { fullLog ->
            if (continuation.isActive) { continuation.resume(fullLog) }
        }
    }


    private fun runCurrentConfig(onFinished: (String) -> Unit) {
        val runManager = RunManager.getInstance(project)

        val selected = runManager.selectedConfiguration

        if (selected == null) {
            Logger.log("Setup run configuration for run project", SenderType.LOGGER, MessageType.WARNING)
            return
        }

        try {
            val executor = DefaultRunExecutor.getRunExecutorInstance()

            val environment = ExecutionEnvironmentBuilder.create(executor, selected).build()

            val outputBuffer = StringBuilder()

            environment.setCallback(object : ProgramRunner.Callback {
                override fun processStarted(descriptor: RunContentDescriptor) {
                    val handler = descriptor.processHandler ?: return

                    handler.addProcessListener(object : ProcessListener {
                        override fun onTextAvailable(event: ProcessEvent, outputType: com.intellij.openapi.util.Key<*>) {
                            if (com.intellij.execution.process.ProcessOutputTypes.SYSTEM != outputType) {
                                outputBuffer.append(event.text)
                            }
                        }

                        override fun processTerminated(event: ProcessEvent) {
                            onFinished(outputBuffer.toString())
                        }
                    })
                }
            })

            ExecutionManager.getInstance(project).restartRunProfile(environment)


        } catch (e: Exception) {
            Logger.log("Run Project error: $e", SenderType.LOGGER, MessageType.ERROR)
        }
    }
}