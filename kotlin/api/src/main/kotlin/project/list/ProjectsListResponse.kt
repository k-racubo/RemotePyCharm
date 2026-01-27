package project.list

import core.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("PROJECTS_LIST_RESPONSE")
data class ProjectsListResponse(
    override val requestId: String,
    override val success: Boolean = true,
    val projects: List<ProjectInfo>?,
    val currentProject: ProjectInfo?
) : Response()