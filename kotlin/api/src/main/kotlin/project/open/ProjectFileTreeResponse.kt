package project.open

import core.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
@SerialName("PROJECT_FILE_TREE_RESPONSE")
data class ProjectFileTreeResponse(
    override val requestId: String,
    override val success: Boolean,
    val fileTree: JsonObject
) : Response()