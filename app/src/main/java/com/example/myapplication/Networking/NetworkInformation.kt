package com.example.myapplication.Networking

data class NetworkInformation (
    val ip: String,
    val port: Int,
    val type: String
) {
    companion object NetworkInfoFactory{

        fun getNetworkInfo(): NetworkInformation{
            return NetworkInformation("10.0.2.2", 5000, "quiz_server")
        }
    }
}