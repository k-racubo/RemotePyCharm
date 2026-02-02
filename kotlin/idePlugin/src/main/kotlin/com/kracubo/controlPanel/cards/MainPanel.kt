package com.kracubo.controlPanel.cards

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBUI
import com.kracubo.controlPanel.components.LogControlPanel
import com.kracubo.controlPanel.components.LogWindow
import com.kracubo.controlPanel.logger.Logger
import com.kracubo.controlPanel.logger.MessageType
import com.kracubo.controlPanel.logger.SenderType
import com.kracubo.events.localServer.IsServerStartedChangedListener
import com.kracubo.events.localServer.IsServerStartedChangedTopics
import com.kracubo.events.localServer.ServerDownListener
import com.kracubo.events.localServer.ServerDownTopics
import com.kracubo.events.localServer.ServerStartedState
import com.kracubo.events.localServer.UnexpectedServerDown
import com.kracubo.networking.localServer.LocalWebSocketServer
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.SwingUtilities

class MainPanel : JPanel(), Disposable {

    val label: JBLabel

    val mainButtonsCard: MainButtonsCard
    val localServerButtonsCard: LocalServerButtonsCard
    val remoteServerButtonsCard: RemoteServerButtonsCard

    val logWindow: LogWindow
    val logControlPanel: LogControlPanel

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        border = JBUI.Borders.empty(10)

        label = JBLabel("Choose connection type").apply {
            alignmentX = CENTER_ALIGNMENT
            font = JBUI.Fonts.label(16f)
        }

        mainButtonsCard = MainButtonsCard(
            onLocalServerBtn = {
                SwingUtilities.invokeLater {
                    mainButtonsCard.isVisible = false
                    localServerButtonsCard.isVisible = true
                    this@MainPanel.revalidate()
                    this@MainPanel.repaint()
                }

                Logger.log("\"Local Server\" button action", SenderType.LOGGER)
            },
            onRemoteServerBtn = {
                SwingUtilities.invokeLater {
                    mainButtonsCard.isVisible = false
                    remoteServerButtonsCard.isVisible = true
                    this@MainPanel.revalidate()
                    this@MainPanel.repaint()
                }

                Logger.log("\"Remote Server\" button action", SenderType.LOGGER)
            })

        localServerButtonsCard = LocalServerButtonsCard(
            onBackBtn = {
                SwingUtilities.invokeLater {
                    mainButtonsCard.isVisible = true
                    localServerButtonsCard.isVisible = false
                    this@MainPanel.revalidate()
                    this@MainPanel.repaint()
                }

                Logger.log("\"Back\" button action from local server", SenderType.LOGGER)
            },
            onServerStateChanged = { state: Boolean ->
                mainButtonsCard.remoteServerBtn.isEnabled = !state

                if (remoteServerButtonsCard.isVisible && state) {
                    SwingUtilities.invokeLater {
                        mainButtonsCard.isVisible = true
                        remoteServerButtonsCard.isVisible = false
                        this@MainPanel.revalidate()
                        this@MainPanel.repaint()
                    }
                }
            }
        )

        remoteServerButtonsCard = RemoteServerButtonsCard(
            onBackBtn = {
                SwingUtilities.invokeLater {
                    mainButtonsCard.isVisible = true
                    remoteServerButtonsCard.isVisible = false
                    this@MainPanel.revalidate()
                    this@MainPanel.repaint()
                }

                Logger.log("\"Back\" button action from remote server", SenderType.LOGGER)
            },
            onServerStateChanged = { state ->
                mainButtonsCard.localServerBtn.isEnabled = !state

                if (localServerButtonsCard.isVisible && state) {
                    SwingUtilities.invokeLater {
                        mainButtonsCard.isVisible = true
                        localServerButtonsCard.isVisible = false
                        this@MainPanel.revalidate()
                        this@MainPanel.repaint()
                    }
                }
            }
        )

        logWindow = LogWindow()
        logControlPanel = LogControlPanel()

        add(label)
        add(Box.createVerticalStrut(20))
        add(mainButtonsCard)
        add(localServerButtonsCard)
        add(remoteServerButtonsCard)
        add(Box.createVerticalStrut(20))
        add(logWindow)
        add(Box.createVerticalStrut(20))
        add(logControlPanel)

        subscribeToServerEvents()

        localServerButtonsCard.isLocalServerStarted = LocalWebSocketServer.getInstance().isServerStarted
    }

    private fun subscribeToServerEvents() {
        ApplicationManager.getApplication().messageBus.connect(this)
            .subscribe(ServerDownTopics.UNEXPECTED_SERVER_DOWN,
            object : ServerDownListener {
                override fun onUnexpectedServerDown(event: UnexpectedServerDown) { handleServerCrashed(event) } })

        ApplicationManager.getApplication().messageBus.connect(this)
            .subscribe(IsServerStartedChangedTopics.SERVER_STATE_CHANGED,
                object : IsServerStartedChangedListener {
                    override fun onServerStateChanged(event: ServerStartedState) {
                        localServerButtonsCard.isLocalServerStarted = event.newValue
                    }
                })
    }

    private fun handleServerCrashed(event: UnexpectedServerDown) {
        SwingUtilities.invokeLater {
            Logger.log("${event.error} port: ${event.port}", SenderType.LOCAL_SERVER,
                messageType = MessageType.ERROR)

            localServerButtonsCard.isLocalServerStarted = false
        }
    }

    override fun dispose() {}
}