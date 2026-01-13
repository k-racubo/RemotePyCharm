package project

import core.Command
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("GET_PROJECTS_LIST")
data class GetProjectsList(
    override val requestId: String,
) : Command()