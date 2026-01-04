package com.kracubo.controlPanel.cards

import com.intellij.openapi.application.EDT
import com.intellij.util.ui.JBUI
import com.kracubo.controlPanel.components.BackBtn
import com.kracubo.controlPanel.components.PortField
import com.kracubo.controlPanel.layout.DynamicGridLayout
import com.kracubo.controlPanel.logger.Logger
import com.kracubo.controlPanel.logger.MessageType
import com.kracubo.controlPanel.logger.SenderType
import com.kracubo.networking.localServer.LocalWebSocketServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.SwingUtilities

class LocalServerButtonsCard(
    onBackBtn: () -> Unit,
    private val onServerStateChanged: (Boolean) -> Unit
) : JPanel() {

    val pluginScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    var dynamicGridLayout = DynamicGridLayout(2, 2, 15, 10)

    val backBtn: BackBtn

    val stopServerBtn: JButton

    val portField: PortField

    val startServerBtn: JButton

    var isLocalServerStarted: Boolean = false
        set(value) {
            field = value
            onServerStateChanged(value)
            updateUiVisibilityAndLayout(value)
        }

    init {
        layout = dynamicGridLayout

        maximumSize = JBUI.size(Int.MAX_VALUE, 80)

        isVisible = false

        backBtn = BackBtn(onClick = { onBackBtn() })

        stopServerBtn = JButton("Stop Server").apply {
            isVisible = false

            addActionListener {
                Logger.log("\"Stop Server\" button action", SenderType.LOGGER)

                pluginScope.launch {
                    LocalWebSocketServer.stop()

                    withContext(Dispatchers.EDT) {
                        isLocalServerStarted = false
                    }
                }
            }
        }

        portField = PortField(onIsValidPortState = {
            when (portField.isValidPort) {
                true -> startServerBtn.isEnabled = true
                false -> startServerBtn.isEnabled = false
            }
        })

        startServerBtn = JButton("Start Server").apply {
            isEnabled = false

            addActionListener {
                Logger.log("\"Start Server\" button action", SenderType.LOGGER)

                pluginScope.launch {
                    val isStarted = LocalWebSocketServer.start(portField.portTextField.text.toInt())

                    withContext(Dispatchers.EDT) {
                        if (isStarted) {
                            Logger.log("Local server started! localhost:${portField.portTextField.text}",
                                senderType = SenderType.LOCAL_SERVER)
                        }else {
                            Logger.log("Local server not started! Possible reason: ${portField.portTextField.text} busy",
                                senderType = SenderType.LOCAL_SERVER, messageType = MessageType.ERROR)
                        }

                        isLocalServerStarted = isStarted
                    }
                }
            }
        }

        add(backBtn)
        add(stopServerBtn)
        add(portField)
        add(startServerBtn)
    }

    private fun updateUiVisibilityAndLayout(serverState: Boolean) {
        SwingUtilities.invokeLater {
            stopServerBtn.isVisible = serverState

            removeAll()

            dynamicGridLayout.dynamicRows = if (serverState) 1 else 2
            layout = dynamicGridLayout

            maximumSize = JBUI.size(Int.MAX_VALUE, if (serverState) 35 else 80)

            add(backBtn)
            add(stopServerBtn)

            if (!serverState) {
                add(portField)
                add(startServerBtn)
            }
        }
    }
}