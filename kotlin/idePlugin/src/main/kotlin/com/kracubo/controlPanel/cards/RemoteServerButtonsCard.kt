package com.kracubo.controlPanel.cards

import com.intellij.util.ui.JBUI
import com.kracubo.controlPanel.components.AuthTokenField
import com.kracubo.controlPanel.components.BackBtn
import com.kracubo.controlPanel.components.IpField
import com.kracubo.controlPanel.components.PortField
import com.kracubo.controlPanel.layout.DynamicGridLayout
import com.kracubo.controlPanel.logger.Logger
import com.kracubo.controlPanel.logger.SenderType
import java.awt.GridLayout
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.SwingUtilities
import kotlin.Unit

class RemoteServerButtonsCard(
    onBackBtn: () -> Unit,
    private val onServerStateChanged: (Boolean) -> Unit
) : JPanel() {

    val dynamicGridLayout = DynamicGridLayout(5, 1, 0, 10)

    val backBtn: BackBtn

    val disconnectBtn: JButton

    private val firstRow: JPanel

    val ipField: IpField
    val portField: PortField
    val authTokenField: AuthTokenField

    val connectBtn: JButton

    private var isRemoteServerConnected: Boolean = false
        set(value) {
            field = value
            onServerStateChanged(value)
            updateUiAndLayout(value)
        }

    init {
        layout = dynamicGridLayout
        isVisible = false

        maximumSize = JBUI.size(Int.MAX_VALUE, 230)

        backBtn = BackBtn(onClick = { onBackBtn() })

        disconnectBtn = JButton("Disconnect").apply {
            isVisible = false

            addActionListener {
                Logger.log("\"Disconnect\" button action", SenderType.LOGGER)
                // TODO create disconnect logic from remote server
                isRemoteServerConnected = false // fucking заглушка
            }
        }

        firstRow = JPanel().apply {
            layout = GridLayout(1, 2, 15, 0)

            maximumSize = JBUI.size(Int.MAX_VALUE, 40)

            add(backBtn)
            add(disconnectBtn)
        }

        ipField = IpField(onIsValidIpState = { checkAllStates() })

        portField = PortField(onIsValidPortState = { checkAllStates() })

        // overriding portField class for best UI in this card
        portField.removeAll()

        portField.layout = BoxLayout(portField, BoxLayout.X_AXIS)

        portField.add(Box.createHorizontalGlue())
        portField.add(Box.createHorizontalStrut(5))
        portField.add(portField.portFieldLabel)
        portField.add(Box.createHorizontalStrut(50))
        portField.add(portField.portTextField)
        portField.add(Box.createHorizontalStrut(5))
        portField.add(Box.createHorizontalGlue())
        // blyatsikii kostili

        authTokenField = AuthTokenField(onIsValidAuthTokenState = { checkAllStates() })

        connectBtn = JButton("Connect").apply {
            isEnabled = false

            addActionListener {
                Logger.log("\"Connect\" button action", SenderType.LOGGER)
                // TODO create logic for remote connection
                isRemoteServerConnected = true // fucking заглушка
            }
        }

        add(firstRow)
        add(ipField)
        add(portField)
        add(authTokenField)
        add(connectBtn)
    }

    private fun checkAllStates() {
        connectBtn.isEnabled = ipField.isValidIp && portField.isValidPort && authTokenField.isValidAuthToken
    }

    private fun updateUiAndLayout(serverState: Boolean) {
        SwingUtilities.invokeLater {
            disconnectBtn.isVisible = serverState

            removeAll()

            dynamicGridLayout.dynamicRows = if (serverState) 1 else 5
            layout = dynamicGridLayout
            maximumSize = JBUI.size(Int.MAX_VALUE, if (serverState) 35 else 230)

            add(firstRow)

            if (!serverState) {
                add(ipField)
                add(portField)
                add(authTokenField)
                add(connectBtn)
            }
        }
    }
}