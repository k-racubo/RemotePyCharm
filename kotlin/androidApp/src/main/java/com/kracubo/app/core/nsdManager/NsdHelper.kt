package com.kracubo.app.core.nsdManager

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
class NsdHelper(private val context: Context) {
    private val nsdManager: NsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
    interface DiscoveryListener {
        fun onServiceFound()
        fun onManualConnect(ip: String, port: Int)
        fun onError()
    }
    var listener: DiscoveryListener? = null
    fun discoverServices() {
        val serviceType = "_remotepycharm._tcp"
        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, object : NsdManager.DiscoveryListener {
            override fun onDiscoveryStarted(regType: String?) {
                Log.i("NsdHelper", "Discovery started")
            }
            override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
                Log.i("NsdHelper", "Service found: ${serviceInfo?.serviceName}")
                serviceInfo?.let {
                    nsdManager.resolveService(it, object : NsdManager.ResolveListener {
                        override fun onResolveFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
                            Log.e("NsdHelper", "Resolve failed: $errorCode")
                            listener?.onError()
                        }
                        override fun onServiceResolved(serviceInfo: NsdServiceInfo?) {
                            Log.i("NsdHelper", "Service resolved: ${serviceInfo?.serviceName}")
                            val host = serviceInfo?.host?.hostAddress
                            val port = serviceInfo?.port
                            val prefs = context.getSharedPreferences("local_search_prefs", Context.MODE_PRIVATE)
                            prefs.edit().apply {
                                putString("LOCAL_SEARCH_IP", host)
                                putInt("LOCAL_SEARCH_PORT", port!!)
                                apply()
                            }
                            Log.i("NsdHelper", "Host: $host Port: $port")
                            listener?.onServiceFound()
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
                listener?.onError()
            }
            override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {
                Log.e("NsdHelper", "Stop discovery failed: $errorCode")
                listener?.onError()
            }
            fun connectManually(ip: String, port: Int){
                Log.i("nsdhelper", "connectiong to localserver with IP $ip and port $port")
                listener?.onManualConnect(ip,port)
            }
        })
    }
}
