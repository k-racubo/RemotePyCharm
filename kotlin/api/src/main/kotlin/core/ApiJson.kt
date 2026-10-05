package core

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

object ApiJson {
    val instance = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    val pretty = Json {
        prettyPrint = true
        prettyPrintIndent = "  "
    } // only for logging
}