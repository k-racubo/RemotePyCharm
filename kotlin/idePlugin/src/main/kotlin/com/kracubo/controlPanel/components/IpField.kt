package com.kracubo.controlPanel.components

import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.JBUI
import java.awt.Color
import java.awt.GridLayout
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.SwingConstants
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class IpField(
    private val onIsValidIpState: () -> Unit
) : JPanel() {

    private val ipFieldLabel: JBLabel
    private val ipTextField: JBTextField

    var isValidIp: Boolean = false
        set(value) {
            field = value
            onIsValidIpState()
        }



    init {
        layout = BoxLayout(this, BoxLayout.X_AXIS)
        maximumSize = JBUI.size(Int.MAX_VALUE, 50)

        ipFieldLabel = JBLabel("ip: ").apply {
            font = JBUI.Fonts.label(14f)
            horizontalAlignment = SwingConstants.RIGHT
        }

        ipTextField = JBTextField("").apply {
            horizontalAlignment = SwingConstants.LEFT
            alignmentY = 0.5f

            toolTipText = "ip field"

            isEditable = true

            document.addDocumentListener(object : DocumentListener {
                override fun insertUpdate(e: DocumentEvent) { validateIpField() }
                override fun removeUpdate(e: DocumentEvent) { validateIpField() }
                override fun changedUpdate(e: DocumentEvent) { validateIpField() }

                private fun validateIpField() {
                    val text = text.trim()

                    isValidIp = when {
                        text.equals("localhost", ignoreCase = true) -> true

                        text.matches(Regex("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")) -> {
                            text.split(".").all { part ->
                                part.toIntOrNull() in 0..255
                            }
                        }
                        else -> false
                    }

                    if (isValidIp) clearError() else showError("Invalid host")
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
        add(Box.createHorizontalGlue())
        add(Box.createHorizontalStrut(5))
        add(ipFieldLabel)
        add(Box.createHorizontalStrut(65))
        add(ipTextField)
        add(Box.createHorizontalStrut(5))
        add(Box.createHorizontalGlue())
    }

}