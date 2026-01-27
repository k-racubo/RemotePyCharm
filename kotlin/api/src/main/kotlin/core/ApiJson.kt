package core

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import project.list.GetProjectsList
import project.list.ProjectsListResponse
import project.open.OpenProjectCommand
import project.open.ProjectFileTreeResponse
import project.run.ResultOfRunResponse
import project.run.RunCurrentConfigCommand

object ApiJson {
    val instance = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        classDiscriminator = "type"
        isLenient = true

        serializersModule = SerializersModule {
            polymorphic(Command::class) {
                subclass(GetProjectsList::class)
                subclass(OpenProjectCommand::class)
                subclass(RunCurrentConfigCommand::class)
            }

            polymorphic(Response::class) {
                subclass(ProjectsListResponse::class)
                subclass(ProjectFileTreeResponse::class)
                subclass(ResultOfRunResponse::class)
            }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    val pretty = Json {
        prettyPrint = true
        prettyPrintIndent = "  "
    } // only for logging
}