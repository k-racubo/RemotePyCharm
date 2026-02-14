package com.kracubo.controlPanel.components

import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.JBUI
import java.awt.Color
import java.awt.GridLayout
import javax.swing.JPanel
import javax.swing.SwingConstants
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class PortField(
    private val onIsValidPortState: () -> Unit
) : JPanel() {

    val portFieldLabel: JBLabel

    val portTextField: JBTextField

    var isValidPort: Boolean = false
        set(value) {
            field = value
            onIsValidPortState()
        }

    init {
        layout = GridLayout(1, 2, 15, 0)
        maximumSize = JBUI.size(Int.MAX_VALUE, 50)

        portFieldLabel = JBLabel("Port: ").apply {
            font = JBUI.Fonts.label(14f)
            horizontalAlignment = SwingConstants.RIGHT
        }

        portTextField = JBTextField("").apply {
            horizontalAlignment = SwingConstants.LEFT
            alignmentY = 0.5f

            toolTipText = "port field"

            isEditable = true

            document.addDocumentListener(object : DocumentListener {
                override fun insertUpdate(e: DocumentEvent) { validatePortField() }
                override fun removeUpdate(e: DocumentEvent) { validatePortField() }
                override fun changedUpdate(e: DocumentEvent) { validatePortField() }

                private fun validatePortField() {
                    when {
                        text.isEmpty() -> {
                            showError("Port cannot be empty")
                            isValidPort = false
                        }

                        !text.matches(Regex("\\d+")) -> {
                            showError("Only digits allowed")
                            isValidPort = false
                        }

                        text.length > 5 -> {
                            showError("Max 5 digits (1-65535)")
                            isValidPort = false
                        }

                        else -> {
                            val value = text.toIntOrNull()
                            if (value == null || value !in 1..65535) {
                                showError("Port must be 1-65535")
                                isValidPort = false
                            } else {
                                clearError()
                                isValidPort = true
                            }
                        }
                    }
                }

                private fun showError(message: String) {
                    background = JBColor(
                        Color(255, 230, 230),
                        Color(80, 50, 50)
                    )
                    toolTipText = message
                }

                private fun clearError() {
                    background = null
                    toolTipText = null
                }
            })

        }

        add(portFieldLabel)
        add(portTextField)
    }

}