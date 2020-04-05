package com.example.myapplication.Networking

import android.content.Context
import android.net.wifi.WifiManager
import android.os.StrictMode
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress


class UDPClient: Client {

    override fun sendMessage(message: String, host: String, port: Int) {
        val socket = DatagramSocket()
        println("Message: $message sent to $host at port $port")
        val data = message.toByteArray(Charsets.UTF_8)
        try {
            val packet = DatagramPacket(data, data.size, InetAddress.getByName(host), port)
            socket.send(packet)
        } catch (e: Exception) {
            println(e.toString())
            e.printStackTrace()
        }
    }

    // The below code was taken from caspii's answer at
    // https://stackoverflow.com/questions/17308729/send-broadcast-udp-but-not-receive-it-on-other-android-devices

    fun BroadcastMessage(message: String, port: Int, context: Context) {
        val socket = DatagramSocket()
        socket.broadcast = true
        println("Message: $message was broadcasted to entire network")
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val data = message.toByteArray(Charsets.UTF_8)
        try {
            val packet = DatagramPacket(data, data.size, getBroadcastAddress(context), port)
            socket.send(packet)
        } catch (e: Exception) {
            println(e.toString())
            e.printStackTrace()
        }
    }

    fun getBroadcastAddress(context: Context): InetAddress {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val dhcp = wifiManager.dhcpInfo
        val broadcast = dhcp.ipAddress and dhcp.netmask or dhcp.netmask.inv()
        val quads = ByteArray(4)
        for (k in 0..3) quads[k] = (broadcast shr k * 8 and 0xFF).toByte()
        return InetAddress.getByAddress(quads)
    }
}