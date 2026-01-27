package com.kracubo.events.logwindow

import com.intellij.util.messages.Topic

data class LogEntry(val message: String)

interface AppLogListener {
    fun onLogAdded(entry: LogEntry)
}

object AppLogTopics {
    val LOG_EVENT = Topic.create("AppLogEvent", AppLogListener::class.java)
}