package core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("GetProjectsListCommand")
data class GetProjectsListCommand(
    override val requestId: String,
) : Command()