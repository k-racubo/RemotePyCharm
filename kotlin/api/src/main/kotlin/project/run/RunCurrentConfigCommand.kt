package project.run

import core.Command
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("RUN_CURRENT_CONFIG_COMMAND")
data class RunCurrentConfigCommand(override val requestId: String) : Command()
