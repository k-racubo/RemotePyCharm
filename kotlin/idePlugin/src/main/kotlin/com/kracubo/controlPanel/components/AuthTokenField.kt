package com.kracubo.controlPanel.components

import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.JBUI
import java.awt.Color
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.SwingConstants
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class AuthTokenField(
    private val onIsValidAuthTokenState: () -> Unit
) : JPanel(){

    private val authTokenLabel: JBLabel
    private val authTokenField: JBTextField


    var isValidAuthToken: Boolean = false
        set(value) {
            field = value
            onIsValidAuthTokenState()
        }

    init {
        layout = BoxLayout(this, BoxLayout.X_AXIS)
        maximumSize = JBUI.size(Int.MAX_VALUE, 50)

        authTokenLabel = JBLabel("AuthToken:").apply {
            font = JBUI.Fonts.label(14f)
            horizontalAlignment = SwingConstants.RIGHT
        }

        authTokenField = JBTextField("").apply {
            horizontalAlignment = SwingConstants.LEFT
            alignmentY = 0.5f

            toolTipText = "authToken field"

            isEditable = true

            document.addDocumentListener(object : DocumentListener {
                override fun insertUpdate(e: DocumentEvent?) { validateAuthToken() }
                override fun removeUpdate(e: DocumentEvent?) { validateAuthToken() }
                override fun changedUpdate(e: DocumentEvent?) { validateAuthToken() }

                private fun validateAuthToken() {
                    when {
                        text.isEmpty() -> {
                            showError()
                            isValidAuthToken = false
                        }
                        else -> {
                            clearError()
                            isValidAuthToken = true
                        }
                    }
                }

                private fun showError() {
                    background = JBColor(
                        Color(255, 230, 230),
                        Color(80, 50, 50)
                    )
                    toolTipText = "Auth token cannot be empty"
                }

                private fun clearError() {
                    background = null
                    toolTipText = null
                }
            })
        }

        add(Box.createHorizontalGlue())
        add(Box.createHorizontalStrut(5))
        add(authTokenLabel)
        add(Box.createHorizontalStrut(14))
        add(authTokenField)
        add(Box.createHorizontalStrut(5))
        add(Box.createHorizontalGlue())
    }
}