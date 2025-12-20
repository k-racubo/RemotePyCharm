import kotlinx.serialization.Serializable

@Serializable
data class PluginServerInfo(
    val name: String,
    val version: String,
)