package com.kracubo.app.core.networking

import com.kracubo.app.core.networking.handlers.Handler
import core.ApiJson
import core.Command
import core.Event
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import project.WelcomePacket
import kotlin.time.Duration.Companion.seconds

object Client {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val httpClient by lazy {
        HttpClient(CIO) {
            install(WebSockets) { pingInterval = 15.seconds }

            install(ContentNegotiation) { json() }
        }
    }

    private var currentSession: DefaultClientWebSocketSession? = null

    suspend fun connect(ip: String, port: Int, token: String? = null) : Boolean {
        val connectionResult = CompletableDeferred<Boolean>()

        scope.launch {
            currentSession?.cancel()

            try {
                httpClient.webSocket(host = ip, port = port, path = "/ws") {
                    handleConnection(this, token, connectionResult)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                connectionResult.complete(false)
            } finally {
                currentSession = null
            }
        }

        return try {
            withTimeout(2000) { connectionResult.await() }
        } catch (_: Exception) {
            false
        }
    }

    fun disconnect() {
        scope.launch {
            currentSession?.close(CloseReason(CloseReason.Codes.NORMAL,
                "User initiated disconnect"))
            currentSession = null
            println("Disconnected by user")
        }
    }

    private suspend fun handleConnection(session: WebSocketSession, token: String?,
                                         connResult: CompletableDeferred<Boolean>) {
        currentSession = session as DefaultClientWebSocketSession

        try {
            val firstFrame = session.incoming.receive()

            if (firstFrame is Frame.Text) {
                if (ApiJson.instance.decodeFromString<Event>(firstFrame.readText()) !is WelcomePacket) {
                    session.close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT,
                        "Invalid Welcome Packet"))
                    connResult.complete(false)
                    return
                }
            } else {
                session.close(CloseReason(CloseReason.Codes.PROTOCOL_ERROR,
                    "Expected text frame"))
                connResult.complete(false)
                return
            }
        } catch (e: Exception) {
            session.close(CloseReason(CloseReason.Codes.PROTOCOL_ERROR,
                "Issues with packet data. Error: ${e.message}"))
            connResult.complete(false)
            return
        }

        connResult.complete(true)

        if (token == null) {
            for (frame in session.incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()

                    Handler.resolve(text)
                }
            }
            currentSession?.cancel()
            currentSession = null
            // disconnect
        }
    }

    suspend fun sendPacket(request: Command) : Boolean? {
        return if (currentSession != null && currentSession?.isActive == true) {
            val requestStr = ApiJson.instance.encodeToString<Command>(request)
            currentSession?.send(requestStr)
            true
        } else false
    }
}