package com.example.myapplication.Networking

import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.text.format.Formatter
import java.util.*

data class NetworkInformation (
    val ip: String,
    val port: Int,
    val type: String
) {
    companion object NetworkInfoFactory{

        fun getNetworkInfo(context: Context): NetworkInformation{
            val wifiManager = context.getSystemService(WIFI_SERVICE) as WifiManager
            val ip = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)

            return NetworkInformation(ip, 6000, "quiz_server")
        }
    }
}