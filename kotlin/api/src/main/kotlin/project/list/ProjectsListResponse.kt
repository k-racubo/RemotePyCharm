package core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import project.list.ProjectInfo

@Serializable
@SerialName("ProjectsListResponse")
data class ProjectsListResponse(
    override val requestId: String,
    val projects: List<ProjectInfo>?,
    val currentProject: ProjectInfo?
) : Response()