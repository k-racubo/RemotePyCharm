package com.kracubo.app.core.networking

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
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
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

    fun connect(ip: String, port: Int, token: String? = null) {
        scope.launch {
            currentSession?.cancel()

            try {
                httpClient.webSocket(host = ip, port = port, path = "/ws") {
                    currentSession = this

                    if (token == null) {
                        for (frame in incoming) {
                            if (frame is Frame.Text) {
                                val text = frame.readText()
                                // логика обработки пакетов
                            }
                        }
                        currentSession?.cancel()
                        currentSession = null
                        // disconnect
                    }
                }
            } catch (_: Exception) {
                // ошибочки
            } finally {
                currentSession = null
            }
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
}