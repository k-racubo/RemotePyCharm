package project.run

import core.Response
import kotlinx.serialization.Serializable

@Serializable
data class ResultOfRunResponse(
    override val requestId: String,
    override val success: Boolean,
    val someShit: String
) : Response()
