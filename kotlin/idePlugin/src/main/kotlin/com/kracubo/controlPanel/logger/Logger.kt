package com.kracubo.controlPanel.logger

import com.intellij.ui.components.JBTextArea
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.swing.SwingUtilities

object Logger {

    private lateinit var logArea: JBTextArea
    private val timestampFormat = DateTimeFormatter.ofPattern("HH:mm:ss")

    fun init(logArea: JBTextArea) {
        Logger.logArea = logArea
    }

    fun log(message: String, senderType: SenderType, messageType: MessageType = MessageType.INFO) {
        SwingUtilities.invokeLater {
            val timestamp = LocalDateTime.now().format(timestampFormat)
            val logMessage = "[$timestamp] $senderType: $message ($messageType)"

            logArea.append("$logMessage\n")

            logArea.caretPosition = logArea.document.length
        }
    }

    fun clearLogWindow() {
        SwingUtilities.invokeLater {
            logArea.text = ""
        }
    }
}