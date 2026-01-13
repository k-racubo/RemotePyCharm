package project

import kotlinx.serialization.Serializable

@Serializable
data class ProjectInfo(
    val projectName: String,
    val projectPath: String,
)