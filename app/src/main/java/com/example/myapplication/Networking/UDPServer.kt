package com.example.myapplication.Networking

import android.content.Context
import java.lang.Exception
import java.net.DatagramSocket
import java.net.DatagramPacket
import java.net.InetAddress

// https://stackoverflow.com/questions/56874545/how-to-get-udp-data-constant-listening-on-kotlin
// https://stackoverflow.com/questions/19540715/send-and-receive-data-on-udp-socket-java-android
// Woodie was very helpful here. Thanks a lot

class UDPServer: Runnable{

    private val listeners = mutableListOf<UDPListener>()

    // Can use composition by setting this to var to be more flexible.
    private val messageSender = UDPClient()

    private fun listenForUDPPackets(port: Int){
        val buffer = ByteArray(4096)
        var socket: DatagramSocket? = null
        try {
            socket = DatagramSocket(6000)
            val packet = DatagramPacket(buffer, buffer.size)
            while (true) {
                socket.receive(packet)
                println("Packet received!")
                for (listener in listeners){
                    listener.onUDP(packet.data.toString())
                }
            }
        }
        catch (e: Exception){
            println(e.toString())
            e.printStackTrace()
        }
    }
    override fun run() {
        println("Listening for UDP packets")
        listenForUDPPackets(5000)
    }

    fun addListener(listener: UDPListener){
        listeners.add(listener)
    }

    fun removeListener(listener: UDPListener){
        listeners.remove(listener)
    }

    fun clearListeners(){
        listeners.clear()
    }
}


