package core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
@SerialName("ProjectFileTreeResponse")
data class ProjectFileTreeResponse(
    override val requestId: String,
    val fileTree: JsonObject
) : Response()