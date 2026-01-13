package com.kracubo.networking.localServer

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.extensions.PluginId
import com.kracubo.controlPanel.logger.Logger
import com.kracubo.controlPanel.logger.MessageType
import com.kracubo.controlPanel.logger.SenderType
import com.kracubo.events.localServer.ServerDownTopics
import com.kracubo.events.localServer.UnexpectedServerDown
import com.kracubo.networking.localServer.handlers.Handler
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.cio.CIO
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.host
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.channels.consumeEach
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.concurrent.thread
import kotlin.time.Duration.Companion.seconds

object LocalWebSocketServer {
    var port: Int? = null
    private var server: EmbeddedServer<*, *>? = null

    private var currentSession: WebSocketSession? = null

    private var isServerStarted: Boolean = false

    private var handler: Handler = Handler()

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

                install(ContentNegotiation) { json() }

                routing {
                    webSocket("/ws") { handleConnection(this, call.request.host()) }

                    get("/crash") {
                        call.respond("server down")
                        UdpListener.stop()
                        server?.stop(0, 0)
                    }
                }
            }

            server?.start(wait = false)

            this.port = port
            isServerStarted = true

            val pluginId = PluginId.getId("com.kracubo.remotepycharm")
            val pluginVersion = PluginManagerCore.getPlugin(pluginId)?.version ?: "1.0.0"

            UdpListener.createMdnsService(port, pluginVersion)

            true
        } catch (e: Exception) {
            println(e)
            false
        }
    }

    fun stop() {
        server?.stop(1000, 5000)

        isServerStarted = false
        port = null
        server = null

        UdpListener.stop()

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

    private suspend fun handleConnection(session: WebSocketSession, hostAddress: String) {

        if (currentSession != null) {
            session.close(
                CloseReason(
                    CloseReason.Codes.TRY_AGAIN_LATER,
                    "Only one connection allowed"))

            Logger.log("host: $hostAddress trying connect to server but socket busy another connection",
                SenderType.LOCAL_SERVER, MessageType.WARNING)
            return
        }

        currentSession = session

        Logger.log("New connection: $hostAddress", SenderType.LOCAL_SERVER)

        session.incoming.consumeEach { frame ->
            if (frame is Frame.Text) {
                val text = frame.readText()

                val response = handler.resolve(text)
                session.send(handler.json.encodeToString(response))
            }
        }

        currentSession = null
        Logger.log("Connection closed: $hostAddress", SenderType.LOCAL_SERVER)
    }
}