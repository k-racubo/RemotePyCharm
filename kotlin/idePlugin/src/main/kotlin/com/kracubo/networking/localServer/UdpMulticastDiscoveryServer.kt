package com.kracubo.networking.localServer

import com.appstractive.dnssd.NetService
import com.appstractive.dnssd.createNetService
import com.kracubo.controlPanel.logger.Logger
import com.kracubo.controlPanel.logger.MessageType
import com.kracubo.controlPanel.logger.SenderType
import java.net.Inet4Address
import java.net.NetworkInterface

object UdpListener {
    private const val SERVICE_TYPE = "_remotepycharm._tcp"
    private const val SERVICE_NAME = "RemotePyCharm local server"

    private var service: NetService? = null

    suspend fun createMdnsService(serverPort: Int, version: String) {
        val targetIpAddress = NetworkInterface.getNetworkInterfaces().asSequence()
            .flatMap { it.inetAddresses.asSequence() }
            .filter { it is Inet4Address && it.hostAddress.startsWith("192.168.") }
            .firstOrNull()?.hostAddress ?: "0.0.0.0"

        System.setProperty("java.rmi.server.hostname", targetIpAddress)

        service = createNetService(
            type = SERVICE_TYPE,
            name = SERVICE_NAME,
            port = serverPort,
            txt = mapOf("version" to version))

        service?.register()

        if (service?.registered == true) {
            Logger.log("Server published in mDNS ($SERVICE_TYPE on port: $serverPort)",
                SenderType.LOCAL_SERVER)
        } else
            Logger.log("mDNS publishing error ($SERVICE_TYPE on port: $serverPort)", SenderType.LOCAL_SERVER,
                MessageType.ERROR)
    }

    suspend fun stop() {
        if (service != null) {
            service?.unregister()
            service = null
            Logger.log("mDNS service stopped", SenderType.LOCAL_SERVER)
        } else
            Logger.log("mDNS not started for stop", SenderType.LOCAL_SERVER, MessageType.WARNING)
    }
}