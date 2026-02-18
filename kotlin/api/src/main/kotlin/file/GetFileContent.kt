package file

import core.Command
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("GetFileContentCommand")
data class GetFileContent(
    override val requestId: String,
    val filePath: String,
) : Command()
