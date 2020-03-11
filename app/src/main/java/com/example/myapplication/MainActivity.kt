package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.zxing.WriterException
import com.android.volley.toolbox.Volley as Volley
import com.example.myapplication.Models.MultipleChoiceResponse
import com.example.myapplication.Networking.*
import com.google.gson.Gson
import org.json.JSONObject

// https://demonuts.com/kotlin-generate-qr-code/ was used for the basis of  QRCode generation and used pretty much all of the code for the QR methods. Great thanks to the authors!
class MainActivity : AppCompatActivity(), UDPListener {
    private var bitmap: Bitmap? = null
    private var imageview: ImageView? = null
    private var generateConnectionQrButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageview = findViewById(R.id.iv)
        generateConnectionQrButton = findViewById(R.id.generate_connection_qr_button)
        val ipEditor = findViewById<EditText>(R.id.ip_enter)
        val setIP = findViewById<Button>(R.id.set_ip)
        var ip = "0.0.0.0"
        setIP.setOnClickListener{
            ip = ipEditor.text.toString()
            Toast.makeText(applicationContext, "Ip is now $ip", Toast.LENGTH_SHORT).show()
        }


        val generateResponseButton = findViewById<Button>(R.id.generate_response)


        /* We don't want to block the UI thread */
        val server = UDPServer()
        server.addListener(this)
        val udpDataListener = Thread(server)
        udpDataListener.start()


        val networkInformation = NetworkInformation.NetworkInfoFactory.getNetworkInfo(this)
        println(networkInformation.ip)
        val udpClient = UDPClient()

        // Change this to 5000 in testing.
        generateResponseButton!!.setOnClickListener {
                Thread(Runnable {
                udpClient.sendMessage("DOG", ip,6000)
        }).start()
        }

        generateConnectionQrButton!!.setOnClickListener {
            try {
                Thread(Runnable {
                    bitmap = QRCodeGenerator.generateInitialConnectionQRCode(500, 500, this)
                    imageview!!.post {
                        imageview!!.setImageBitmap(bitmap)
                    }
                }).start()
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }

    }

    override fun onUDP(data: String) {
        Thread(Runnable {
            println(data)
            runOnUiThread {
                Toast.makeText(applicationContext, data, Toast.LENGTH_SHORT).show()
            }
        }).start()
    }
}
