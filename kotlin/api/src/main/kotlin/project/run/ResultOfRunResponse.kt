package core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ResultOfRunResponse")
data class ResultOfRunResponse(
    override val requestId: String,
    val result: String
) : Response()
