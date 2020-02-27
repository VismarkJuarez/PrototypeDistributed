package com.example.myapplication

import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.graphics.Bitmap
import android.net.wifi.WifiManager
import android.text.format.Formatter
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.google.gson.Gson


// Thanks to https://www.callicoder.com/generate-qr-code-in-java-using-zxing/
// https://stackoverflow.com/questions/28232116/android-using-zxing-generate-qr-code/30529128
// https://demonuts.com/kotlin-generate-qr-code/
class QRCodeGenerator {
    companion object QRFactory {
        val gson = Gson()
        fun MakeQRCode(text: String, width: Int, height: Int, context: Context): Bitmap {
            val bitmatrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height)
            val pixels = IntArray(width * height)
            for (y in 0 until bitmatrix.height){
                val offset = y * bitmatrix.width
                for (x in 0 until bitmatrix.width){
                    pixels[offset + x] = if (bitmatrix.get(x, y))
                        ContextCompat.getColor(context, R.color.black)
                    else
                        ContextCompat.getColor(context, R.color.white)
                }
            }
            return Bitmap.createBitmap(bitmatrix.width, bitmatrix.height, Bitmap.Config.ARGB_8888).also {
                it.setPixels(pixels, 0, 500, 0, 0, height, width)
            }
        }
        fun generateInitialConnectionQRCode(width: Int, height: Int, context: Context): Bitmap{
            val wm =
                context.getSystemService(WIFI_SERVICE) as WifiManager?
            val ip =
                Formatter.formatIpAddress(wm!!.connectionInfo.ipAddress)
            val connectionInfo = gson.toJson(ConnectionInfo(ip, 5000, "quiz_primary_server"))
            return MakeQRCode(text = connectionInfo, width=500, height = 500, context = context)
        }
    }
}

data class ConnectionInfo (
    val ip: String,
    val port: Int,
    val type: String
)
