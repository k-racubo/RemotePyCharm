package file

import core.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("FileContentResponse")
data class FileContentResponse(
    override val requestId: String,
    override val success: Boolean,
    val content: List<String>
) : Response()
