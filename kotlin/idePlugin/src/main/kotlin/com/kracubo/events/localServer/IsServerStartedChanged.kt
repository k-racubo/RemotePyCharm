package com.kracubo.events.localServer

import com.intellij.util.messages.Topic

data class ServerStartedState(val newValue: Boolean)

interface IsServerStartedChangedListener {
    fun onServerStateChanged(event: ServerStartedState)
}

object IsServerStartedChangedTopics {
    val SERVER_STATE_CHANGED = Topic.create(
        "serverStateChanged",
        IsServerStartedChangedListener::class.java
    )
}