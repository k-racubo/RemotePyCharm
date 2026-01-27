package com.kracubo.events.logwindow

import com.intellij.util.messages.Topic

interface ClearLogWindowListener {
    fun onLogClear()
}

object ClearLogWindowTopics {
    val CLEAR_LOG = Topic.create("clearLogEvent", ClearLogWindowListener::class.java)
}