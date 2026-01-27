package project.list

import kotlinx.serialization.Serializable

@Serializable
data class ProjectInfo(
    val projectName: String,
    val projectPath: String,
)