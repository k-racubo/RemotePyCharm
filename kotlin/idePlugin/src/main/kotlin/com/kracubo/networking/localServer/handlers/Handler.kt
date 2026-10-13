package com.kracubo.networking.localServer.handlers

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.kracubo.controlPanel.logger.Logger
import com.kracubo.controlPanel.logger.MessageType
import com.kracubo.controlPanel.logger.SenderType
import core.ApiJson
import core.Command
import core.ErrorResponse
import core.ErrorType
import core.Event
import core.Response
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonPrimitive
import java.util.ServiceLoader
import kotlin.reflect.KClass

@Service(Service.Level.APP)
class Handler {

    companion object { fun getInstance() = service<Handler>() }

    private val handlers: Map<KClass<out Command>, ICommandHandler<*>> =
        ServiceLoader.load(ICommandHandler::class.java, this::class.java.classLoader)
            .associateBy { it.commandClass }

    private val eventHandlers: Map<KClass<out Event>, IEventHandler<*>> =
        ServiceLoader.load(IEventHandler::class.java, this::class.java.classLoader)
            .associateBy { it.eventClass }

    @Suppress("UNCHECKED_CAST")
    suspend fun resolve(message: String): Response? {
        val jsonElement = try {
            ApiJson.instance.parseToJsonElement(message)
        } catch (e: Exception) {
            Logger.log("Invalid JSON: ${e.message}", SenderType.LOCAL_SERVER, MessageType.WARNING)
            return ErrorResponse(
                requestId = "",
                errorType = ErrorType.INVALID_JSON,
                errorMessage = e.message ?: "Invalid JSON",
            )
        }

        if (jsonElement !is JsonObject) {
            return ErrorResponse(
                requestId = "",
                errorType = ErrorType.INVALID_JSON,
                errorMessage = "Expected JSON object"
            )
        }

        val requestId = jsonElement["requestId"]?.jsonPrimitive?.contentOrNull ?: ""

        val command = try {
            ApiJson.instance.decodeFromJsonElement<Command>(jsonElement)
        } catch (_: Exception) {
            null
        }

        if (command != null) {
            val handler = handlers[command::class] as? ICommandHandler<Command>
                ?: return ErrorResponse(
                    requestId = requestId,
                    errorType = ErrorType.HANDLER_NOT_FOUND,
                    errorMessage = "No handler for ${command::class.simpleName}"
                )

            return try {
                handler.handle(command)
            } catch (e: Exception) {
                Logger.log("Handler failed: ${e.message}", SenderType.LOCAL_SERVER, MessageType.ERROR)
                ErrorResponse(
                    requestId = requestId,
                    errorType = ErrorType.HANDLER_FAILED,
                    errorMessage = e.message ?: "Handler failed"
                )
            }
        }

        val event = try {
            ApiJson.instance.decodeFromJsonElement<Event>(jsonElement)
        } catch (_: Exception) {
            null
        }

        if (event != null) {
            val handler = eventHandlers[event::class] as? IEventHandler<Event>
                ?: return ErrorResponse(
                    requestId = requestId,
                    errorType = ErrorType.HANDLER_NOT_FOUND,
                    errorMessage = "No event handler for ${event::class.simpleName}"
                )

            return try {
                handler.handle(event)
            } catch (e: Exception) {
                Logger.log("Event handler failed: ${e.message}", SenderType.LOCAL_SERVER, MessageType.ERROR)
                ErrorResponse(
                    requestId = requestId,
                    errorType = ErrorType.HANDLER_FAILED,
                    errorMessage = e.message ?: "Event handler failed"
                )
            }
        }

        return ErrorResponse(
            requestId = requestId,
            errorType = ErrorType.UNKNOWN_MESSAGE,
            errorMessage = "Unknown message type"
        )
    }
}