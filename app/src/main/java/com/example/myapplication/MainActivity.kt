package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.myapplication.Networking.QuizServer
import com.google.zxing.WriterException
import com.android.volley.toolbox.Volley as Volley
import com.example.myapplication.Models.MultipleChoiceResponse
import com.example.myapplication.Networking.UDPClient
import com.example.myapplication.Networking.UDPListener
import com.example.myapplication.Networking.UDPServer
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
        val queue = Volley.newRequestQueue(this)
        generateConnectionQrButton = findViewById(R.id.generate_connection_qr_button)
        val generateResponseButton = findViewById<Button>(R.id.generate_response)
        val gson = Gson()

        /* We don't want to block the UI thread */
        val server = UDPServer()
        server.addListener(this)
        val udpDataListener = Thread(server)
        udpDataListener.start()


        val udpClient = UDPClient()
        generateResponseButton!!.setOnClickListener {
                Thread(Runnable {
                udpClient.sendMessage("DOG", "10.0.2.2",5000)
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
        /*
        val quizServer = QuizServer()
        quizServer.main()
        val url = "http://10.0.2.2:5000/recordResponse"
        val response = MultipleChoiceResponse(12, 1, "Dog", 12, 2)
        val jsonBody = gson.toJson(response)
        val jsonObject = JSONObject(jsonBody)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, jsonObject, Response.Listener { response -> println("Received")}, Response.ErrorListener { error -> println(error.toString()) })
        val url_2 = "http://10.0.2.2:5000/getResponse/12"
        val jsonObjectRequestTwo = JsonObjectRequest(Request.Method.GET, url_2, null, Response.Listener {  response -> println(response)}, Response.ErrorListener { error -> println(error.toString()) })
        queue.add(jsonObjectRequest)

         */

        // Meant to test if cache works.
    }

    override fun onUDP(data: String) {
        Thread(Runnable {
            println(data)
        }).start()
    }
}
