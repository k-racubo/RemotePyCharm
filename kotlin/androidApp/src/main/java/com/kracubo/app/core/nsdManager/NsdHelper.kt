package com.kracubo.app.core.nsdManager

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log

class NsdHelper(private val context: Context) {
    private val nsdManager: NsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager

    fun discoverServices() {
        val serviceType = "_remotepycharm._tcp"
        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD,  object : NsdManager.DiscoveryListener {
            override fun onDiscoveryStarted(regType: String?)
            {
                Log.i("NsdHelper", "Discovery started")
            }

            override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
                Log.i("NsdHelper", "Service found: ${serviceInfo?.serviceName}")
                serviceInfo?.let {
                    nsdManager.resolveService(it, object : NsdManager.ResolveListener {
                        override fun onResolveFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
                            Log.e("NsdHelper", "Resolve failed: $errorCode")
                        }

                        override fun onServiceResolved(serviceInfo: NsdServiceInfo?) {
                            Log.i("NsdHelper", "Service resolved: ${serviceInfo?.serviceName}")
                            val host = serviceInfo?.host?.hostAddress
                            val port = serviceInfo?.port
                            Log.i("nsdHelper", "Host: ${host} Port:${port}")
                            // Здесь можно использовать host и port для подключения к серверу
                        }
                    })
                }
            }
            override fun onServiceLost(serviceInfo: NsdServiceInfo?) {
                Log.e("NsdHelper", "Service lost: ${serviceInfo?.serviceName}")
            }

            override fun onDiscoveryStopped(serviceType: String?) {
                Log.i("NsdHelper", "Discovery stopped")
            }

            override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {
                Log.e("NsdHelper", "Start discovery failed: $errorCode")
            }

            override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {
                Log.e("NsdHelper", "Stop discovery failed: $errorCode")
            }
        })
    }
}
