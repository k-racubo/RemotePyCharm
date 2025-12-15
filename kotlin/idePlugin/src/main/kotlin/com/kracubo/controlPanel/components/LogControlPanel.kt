package com.kracubo.controlPanel.components

import com.intellij.ide.actions.RevealFileAction
import com.intellij.openapi.project.ProjectManager
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.kracubo.controlPanel.logger.Logger
import java.awt.Color
import java.awt.Cursor
import java.awt.Desktop
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.SwingUtilities

class LogControlPanel : JPanel() {

    private val clearLogBtn: JButton
    private val clickableText: JBLabel

    init {
        layout = BoxLayout(this, BoxLayout.X_AXIS)

        clearLogBtn = JButton("Clear logs").apply {
            toolTipText = "Clear only displayed logs (log files remain unchanged)"

            addActionListener { Logger.clearLogWindow() }
        }

        clickableText = JBLabel("Logs saved in memory..").apply {
            val normalColor = JBColor(
                Color(0, 102, 204),
                Color(100, 180, 255)
            )

            val hoverColor = JBColor(
                Color(255, 102, 0),   // Orange for light theme
                Color(255, 180, 100)  // Light orange for dark theme
            )

            foreground = normalColor

            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)

            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        try {
                            val userHome = System.getProperty("user.home")
                            val logDir = File("$userHome/.RemotePyCharm/logs")

                            if (!logDir.exists()) {
                                logDir.mkdirs()
                            }

                            val project = ProjectManager.getInstance().openProjects.firstOrNull()
                            if (project != null && !project.isDisposed) {
                                RevealFileAction.openDirectory(logDir)
                            } else {
                                Desktop.getDesktop().open(logDir)
                            }

                            // TODO logger log for this action

                        } catch (e: Exception) {
                            // TODO logger log for this action

                            val userHome = System.getProperty("user.home")
                            val logPath = "$userHome/.RemotePyCharm/logs"

                            JOptionPane.showMessageDialog(
                                this@apply,
                                "Log files are stored at:\n$logPath\n\nError: ${e.message}",
                                "Log Directory",
                                JOptionPane.WARNING_MESSAGE
                            )
                        }

                    }
                }

                override fun mouseEntered(e: MouseEvent) {
                    foreground = hoverColor
                }

                override fun mouseExited(e: MouseEvent) {
                    foreground = normalColor
                }
            })

        }

        add(clearLogBtn)
        add(Box.createHorizontalGlue())
        add(clickableText)
    }
}