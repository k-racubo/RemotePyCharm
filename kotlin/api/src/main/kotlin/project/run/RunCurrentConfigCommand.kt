package core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("RunCurrentConfigCommand")
data class RunCurrentConfigCommand(override val requestId: String) : Command()
