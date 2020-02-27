package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.google.zxing.WriterException

// https://demonuts.com/kotlin-generate-qr-code/ was used for the basis of  QRCode generation and used pretty much all of the code for the QR methods. Great thanks to the authors!
class MainActivity : AppCompatActivity() {
    internal var bitmap: Bitmap? = null
    private var imageview: ImageView? = null
    private var generateConnectionQrButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageview = findViewById(R.id.iv)
        generateConnectionQrButton = findViewById(R.id.generate_connection_qr_button)

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

}
