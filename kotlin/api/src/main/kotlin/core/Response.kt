package core

import kotlinx.serialization.Serializable

@Serializable
abstract class Response: API() {
    abstract val success: Boolean
}