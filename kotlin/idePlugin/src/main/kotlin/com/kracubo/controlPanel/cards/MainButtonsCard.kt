package com.kracubo.controlPanel.cards

import com.intellij.util.ui.JBUI
import java.awt.GridLayout
import javax.swing.JButton
import javax.swing.JPanel

class MainButtonsCard(
    onLocalServerBtn: () -> Unit,
    onRemoteServerBtn: () -> Unit
) : JPanel() {

    var localServerBtn: JButton
    var remoteServerBtn: JButton

    init {
        layout = GridLayout(1, 2, 15, 0)

        maximumSize = JBUI.size(Int.MAX_VALUE, 35)

        localServerBtn = JButton("Local Server").apply { addActionListener { onLocalServerBtn() } }

        remoteServerBtn = JButton("Remote Server").apply { addActionListener { onRemoteServerBtn() } }

        add(localServerBtn)
        add(remoteServerBtn)
    }

}