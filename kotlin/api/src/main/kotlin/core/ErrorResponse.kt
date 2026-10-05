package core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ERROR")
data class ErrorResponse(
    override val requestId: String,
    val errorType: String,
    val errorMessage: String
) : Response()