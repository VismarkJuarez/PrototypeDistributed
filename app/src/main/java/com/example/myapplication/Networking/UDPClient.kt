package com.example.myapplication.Networking

import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class UDPClient: Client {

    override fun sendMessage(message: String, host: String, port: Int){
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
}