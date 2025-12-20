package com.kracubo.networking.localServer

import PluginServerInfo
import com.intellij.openapi.application.ApplicationManager
import com.kracubo.controlPanel.logger.Logger
import com.kracubo.controlPanel.logger.SenderType
import com.kracubo.events.localServer.ServerDownTopics
import com.kracubo.events.localServer.UnexpectedServerDown
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.cio.CIO
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.concurrent.thread
import kotlin.time.Duration.Companion.seconds

object LocalWebSocketServer {
    private var port: Int? = null
    private var server: EmbeddedServer<*, *>? = null

    private var isServerStarted: Boolean = false

    init {
        startHealthMonitor()
    }

    fun start(port: Int): Boolean {
        return try {
            server = embeddedServer(CIO, port = port) {
                install(WebSockets) {
                    pingPeriod = 15.seconds
                    timeout = 30.seconds
                }

                install(ContentNegotiation) {
                    json()
                }

                routing {
                    webSocket("/ws") {
                        for (frame in incoming) {
                            when (frame) {
                                is Frame.Text -> {
                                    val message = frame.readText()

                                }

                                else -> {}
                            }
                        }
                        // TODO logic for disconnect
                    }

                    get("/crash") {
                        call.respond("server down")
                        server?.stop(0, 0)
                    }

                    get("/info") {
                        val response = PluginServerInfo("RemotePyCharm", "1.0.0")
                        call.respond(response)
                    }
                }
            }

            server?.start(wait = false)

            this.port = port
            isServerStarted = true

            true
        } catch (_: Exception) {
            false
        }
    }

    fun stop() {
        server?.stop(1000, 5000)

        isServerStarted = false
        port = null
        server = null

        Logger.log("Local server stopped", senderType = SenderType.LOCAL_SERVER)
    }

    private fun startHealthMonitor() {
        thread(isDaemon = true) {
            while (true) {
                Thread.sleep(10000)

                if (isServerStarted) {
                    if (!isServerAlive()) {
                        ApplicationManager.getApplication().messageBus
                            .syncPublisher(ServerDownTopics.UNEXPECTED_SERVER_DOWN)
                            .onUnexpectedServerDown(UnexpectedServerDown(port = port!!))

                        isServerStarted = false
                        port = null
                        server = null
                    }
                }
            }
        }
    }

    private fun isServerAlive(): Boolean {
        return try {
            Socket().use { socket ->
                socket.connect(InetSocketAddress("localhost", port ?: return false),
                    1000)
                true
            }
        } catch (_: Exception) {
            false
        }
    }
}