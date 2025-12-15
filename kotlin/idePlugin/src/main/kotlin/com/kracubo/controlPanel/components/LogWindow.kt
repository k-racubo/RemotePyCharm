package com.kracubo.controlPanel.components

import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.util.ui.JBUI

class LogWindow : JBScrollPane() {

    val logArea: JBTextArea

    init {
        maximumSize = JBUI.size(Int.MAX_VALUE, 500)

        verticalScrollBarPolicy = VERTICAL_SCROLLBAR_AS_NEEDED
        horizontalScrollBarPolicy = HORIZONTAL_SCROLLBAR_NEVER

        logArea = JBTextArea().apply {
            isEditable = false
            lineWrap = true
        }

        setViewportView(logArea)
    }

}