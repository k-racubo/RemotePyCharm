package core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("GetFileContentCommand")
data class GetFileContentCommand(
    override val requestId: String,
    val filePath: String,
) : Command()
