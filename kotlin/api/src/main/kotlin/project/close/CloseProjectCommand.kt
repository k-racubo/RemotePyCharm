package project.close

import core.Command
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("CLOSE_PROJECT_COMMAND")
data class CloseProjectCommand(
    override val requestId: String,
) : Command()