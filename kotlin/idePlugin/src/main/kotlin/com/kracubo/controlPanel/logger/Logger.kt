package com.kracubo.controlPanel.logger

import com.intellij.ui.components.JBTextArea
import java.io.File
import java.io.RandomAccessFile
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors
import javax.swing.SwingUtilities

object Logger {

    private lateinit var logArea: JBTextArea
    private val timestampFormat = DateTimeFormatter.ofPattern("HH:mm:ss")
    private val logDir = Paths.get(
        System.getProperty("user.home"),
        ".remotePyCharm",
        "logs"
    ).toFile()

    private val fileLoggerThread = Executors.newSingleThreadExecutor { runnable ->
        Thread(runnable, "FileLogger").apply { isDaemon = true }
    }

    private var logFile: File? = null
    private const val MAX_LOG_FILE_SIZE = 10 * 1024 * 1024



    fun init(logArea: JBTextArea) {
        Logger.logArea = logArea

        setupLogDirectory()

        logFile = getCurrentLogFile()
    }

    private fun setupLogDirectory() {
        if (!logDir.exists()) {
            logDir.mkdirs()
        }
    }

    private fun getCurrentLogFile(): File {
        val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return File(logDir, "remote-pycharm-$date.log")
    }

    private fun logToFile(logMessage: String) {
        fileLoggerThread.submit {
            try {
                synchronized(this) {
                    val file = logFile ?: return@submit

                    if (file.exists() && file.length() >= MAX_LOG_FILE_SIZE) {
                        rotateLogFile()
                    }

                    RandomAccessFile(file, "rw").use { raf ->
                        raf.seek(raf.length())
                        raf.write(logMessage.toByteArray())
                    }
                }
            } catch (e: Exception) { System.err.println("Failed to write log: ${e.message}") }
        }
    }

    private fun rotateLogFile() {
        val oldFile = logFile ?: return

        var counter = 1
        var archivedFile: File

        do {
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            archivedFile = File(logDir, "remote-pycharm-${timestamp}-${counter}.log")
            counter++
        } while (archivedFile.exists())

        oldFile.renameTo(archivedFile)

        val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        logFile = File(logDir, "remote-pycharm-${date}.log")
    }

    fun log(message: String, senderType: SenderType, messageType: MessageType = MessageType.INFO) {
        val timestamp = LocalDateTime.now().format(timestampFormat)
        val logMessage = "[$timestamp] $senderType: $message ($messageType)"

        SwingUtilities.invokeLater {
            logArea.append("$logMessage\n")
            logArea.caretPosition = logArea.document.length
        }

        logToFile("$logMessage\n")
    }

    fun clearLogWindow() { SwingUtilities.invokeLater { logArea.text = "" } }
}