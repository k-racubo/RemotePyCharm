package com.kracubo.events.localServer

import com.intellij.util.messages.Topic

data class UnexpectedServerDown(
    val port: Int,
    val error: String = "Server shutdown due to unexpected error",
    val timestamp: Long = System.currentTimeMillis()
)

interface ServerDownListener {
    fun onUnexpectedServerDown(event: UnexpectedServerDown)
}

object ServerDownTopics {
    val UNEXPECTED_SERVER_DOWN = Topic.create(
        "Unexpected Server Down",
        ServerDownListener::class.java
    )
}