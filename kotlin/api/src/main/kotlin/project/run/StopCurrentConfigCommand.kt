package project.run

import core.Command
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("STOP_CURRENT_PROJECT_COMMAND")
data class StopCurrentConfigCommand(
    override val requestId: String
) : Command()
