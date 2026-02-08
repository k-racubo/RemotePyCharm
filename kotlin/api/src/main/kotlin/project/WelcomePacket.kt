package project

import core.Event
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("WELCOME")
data class WelcomePacket(
    override val requestId: String = "Welcome",
    val server: String = "remotePyCharm",
    val version: String
) : Event()