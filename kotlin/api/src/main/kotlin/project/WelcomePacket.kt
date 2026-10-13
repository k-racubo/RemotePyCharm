package core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("WelcomePacket")
data class WelcomePacket(
    override val requestId: String = "Welcome",
    val version: String,
    val versionProtocol: Int
) : Event()