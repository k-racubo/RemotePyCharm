package core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("StopCurrentConfigCommand")
data class StopCurrentConfigCommand(
    override val requestId: String
) : Command()
