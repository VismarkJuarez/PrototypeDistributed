package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar

// https://demonuts.com/kotlin-generate-qr-code/ was used for the basis of  QRCode generation.
class MainActivity : AppCompatActivity() {
    internal var bitmap: Bitmap? = null
    private var imageview: ImageView? = null
    private var btn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageview = findViewById(R.id.iv) as ImageView
        btn = findViewById(R.id.btn) as Button

        btn!!.setOnClickListener {
            try {
                bitmap = QRCodeGenerator.generateInitialConnectionQRCode(500, 500, this)
                imageview!!.setImageBitmap(bitmap)
                val path = saveImage(bitmap)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }
    }

    fun saveImage(myBitmap: Bitmap?): String {
        val bytes = ByteArrayOutputStream()
        myBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            Environment.getExternalStorageDirectory().toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs())
            wallpaperDirectory.mkdirs()
        }

        try {
            val f = File(wallpaperDirectory, Calendar.getInstance()
                .timeInMillis.toString() + ".jpg")
            f.createNewFile()   //give read write permission
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                arrayOf(f.path),
                arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)

            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""

    }

    companion object {

        val QRcodeWidth = 500
        private val IMAGE_DIRECTORY = "/QRcodeDemonuts"
    }

}
