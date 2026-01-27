package project.open

import core.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("PROJECT_FILE_TREE_RESPONSE")
data class ProjectFileTreeResponse(
    override val requestId: String,
    override val success: Boolean,
    val fileTree: Map<String, String>
) : Response()