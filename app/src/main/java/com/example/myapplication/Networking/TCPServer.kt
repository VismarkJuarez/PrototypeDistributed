package com.example.myapplication.Networking

class TCPServer : Server{
    override fun listenForPackets(port: Int) {
        println("Okay")
    }

    override fun addListener(listener: UDPListener) {
        println("Okay")
    }

    override fun clearListeners() {
        println("Okay")
    }

    override fun removeListener(listener: UDPListener) {
        println("Okay")
    }

    override fun run() {
        println("Okay")
    }
}