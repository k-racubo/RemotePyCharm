package core

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import project.GetProjectsList
import project.ProjectsListResponse

object ApiJson {
    val instance = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        classDiscriminator = "type"

        serializersModule = SerializersModule {
            polymorphic(Command::class) {
                subclass(GetProjectsList::class)
            }

            polymorphic(Response::class) {
                subclass(ProjectsListResponse::class)
            }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    val pretty = Json {
        prettyPrint = true
        prettyPrintIndent = "  "
    } // only for logging
}