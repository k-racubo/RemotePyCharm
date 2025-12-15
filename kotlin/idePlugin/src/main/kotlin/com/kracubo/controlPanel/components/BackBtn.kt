package com.kracubo.controlPanel.components

import javax.swing.JButton

class BackBtn(onClick: () -> Unit) : JButton("<- Back") {
    init {
        addActionListener {
          onClick()
        }
    }
}