package com.kracubo

import com.intellij.openapi.components.BaseComponent
import com.intellij.openapi.diagnostic.Logger

class MyPlugin : BaseComponent {

    private val log = Logger.getInstance(MyPlugin::class.java)

    @Deprecated("Deprecated in Java")
    override fun initComponent() {
        log.info("Remote PyCharm plugin initialized! 🚀")
    }

    @Deprecated("Deprecated in Java")
    override fun disposeComponent() {
        log.info("Remote PyCharm plugin disposed.")
    }
}