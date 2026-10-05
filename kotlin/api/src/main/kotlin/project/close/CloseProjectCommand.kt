package core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("CloseProjectCommand")
data class CloseProjectCommand(
    override val requestId: String,
) : Command()