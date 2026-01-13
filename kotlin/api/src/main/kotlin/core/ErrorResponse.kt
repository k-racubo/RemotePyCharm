package core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ERROR")
data class ErrorResponse(
    override val requestId: String,
    override val success: Boolean = false,
    val errorCode: String,
    val errorMessage: String
) : Response()