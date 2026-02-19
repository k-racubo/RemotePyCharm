package project.run

import core.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("RESULT_OF_RUN_RESPONSE")
data class ResultOfRunResponse(
    override val requestId: String,
    override val success: Boolean,
    val result: String
) : Response()
