package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import com.google.zxing.WriterException
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import android.os.Environment
import androidx.core.content.ContextCompat

// Thanks to https://www.callicoder.com/generate-qr-code-in-java-using-zxing/
// https://stackoverflow.com/questions/28232116/android-using-zxing-generate-qr-code/30529128
// https://demonuts.com/kotlin-generate-qr-code/
class QRCodeGenerator {
    companion object QRFactory {
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
            return MakeQRCode(text = "STUB", width=500, height = 500, context = context)
        }
    }
}
