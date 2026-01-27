package project.open

import core.Command
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("OPEN_PROJECT_COMMAND")
data class OpenProjectCommand(
    override val requestId: String,
    val projectName: String,
    val projectPath: String,
) : Command()