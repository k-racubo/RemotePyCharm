package com.kracubo.networking.localServer

import com.kracubo.controlPanel.logger.Logger
import com.kracubo.controlPanel.logger.MessageType
import com.kracubo.controlPanel.logger.SenderType
import java.net.Inet4Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.Socket
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo

object UdpListener {
    private const val SERVICE_TYPE = "_remotepycharm._tcp.local."
    private const val SERVICE_NAME = "RemotePyCharm local server"

    private var jmdns: JmDNS? = null
    private var serviceInfo: ServiceInfo? = null

    fun createMdnsService(serverPort: Int, version: String) {
        val targetIpAddress = getRealInterfaceForMdns() ?: run {
            Logger.log("No suitable network interface found", SenderType.LOCAL_SERVER, MessageType.ERROR)
            return
        }

        try {
            jmdns = JmDNS.create(targetIpAddress, SERVICE_NAME)

            serviceInfo = ServiceInfo.create(
                SERVICE_TYPE,
                SERVICE_NAME,
                serverPort,
                0,
                0,
                true,
                mapOf("version" to version)
            )

            jmdns?.registerService(serviceInfo)

            Logger.log("Server published in mDNS ($SERVICE_TYPE on ${targetIpAddress.hostAddress}:$serverPort)",
                SenderType.LOCAL_SERVER)

        } catch (_: Exception) {
            Logger.log("mDNS publishing error ($SERVICE_TYPE on port: $serverPort)", SenderType.LOCAL_SERVER,
                MessageType.ERROR)
        }
    }
    fun stop() {
        try {
            serviceInfo?.let { info ->
                jmdns?.unregisterService(info)
            }

            jmdns?.close()

            jmdns = null
            serviceInfo = null

            Logger.log("JmDNS service stopped", SenderType.LOCAL_SERVER)

        } catch (e: Exception) {
            Logger.log(
                "Error stopping JmDNS: ${e.message}",
                SenderType.LOCAL_SERVER, MessageType.WARNING)
        }
    }

    fun getRealInterfaceForMdns(): InetAddress? {
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 2000)
            val localAddress = socket.localAddress as Inet4Address
            socket.close()

            val iface = NetworkInterface.getByInetAddress(localAddress)

            if (iface != null && iface.isUp && !iface.isLoopback && !iface.isVirtual) {
                localAddress
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}