package core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ERROR")
data class ErrorResponse(
    override val requestId: String,
    val errorType: ErrorType,
    val errorMessage: String
) : Response()

@Serializable
enum class ErrorType {
    INVALID_JSON,
    UNKNOWN_MESSAGE,
    HANDLER_NOT_FOUND,
    HANDLER_FAILED,
    NO_ACTIVE_PROJECT,
    PROJECT_NOT_FOUND,
    NO_RUN_CONFIG,
    INTERNAL_ERROR,


    GET_FILE_CONTENT_FAILED
}