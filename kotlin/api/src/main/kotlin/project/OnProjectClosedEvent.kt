package core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("OnProjectClosedEvent")
data class OnProjectClosedEvent(
    override val requestId: String = "event"
) : Event()
