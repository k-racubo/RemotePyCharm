package com.kracubo.toolwindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.kracubo.controlPanel.cards.MainPanel
import com.kracubo.controlPanel.logger.Logger
import com.kracubo.controlPanel.logger.SenderType

class ControlPanel : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val mainPanel = MainPanel()

        Logger.init()

        Logger.log("Logger initialized", SenderType.LOGGER)

        val content = ContentFactory.getInstance().createContent(mainPanel, "", false)

        toolWindow.contentManager.addContent(content)
    }
}