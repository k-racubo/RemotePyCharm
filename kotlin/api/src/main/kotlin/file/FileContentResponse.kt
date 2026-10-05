package core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("FileContentResponse")
data class FileContentResponse(
    override val requestId: String,
    val content: List<String>
) : Response()
