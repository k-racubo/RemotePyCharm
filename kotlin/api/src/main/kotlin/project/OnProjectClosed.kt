package project

import core.Event
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("OnProjectClosedEvent")
data class OnProjectClosed(
    override val requestId: String = "event"
) : Event()
