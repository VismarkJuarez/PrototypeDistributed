package com.example.myapplication.Networking

import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket

interface Server: Runnable{

    fun listenForPackets(port: Int)

    fun addListener(listener: UDPListener)

    fun removeListener(listener: UDPListener)

    fun clearListeners()
}
