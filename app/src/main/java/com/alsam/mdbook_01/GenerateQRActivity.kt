package com.alsam.mdbook_01

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class GenerateQRActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_qr)

        val qrImageView: ImageView = findViewById(R.id.qrImageView)
        val generateButton: Button = findViewById(R.id.generateButton)

        generateButton.setOnClickListener {
            // Retrieving userID from SharedPreferences
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val userID = sharedPreferences.getString("USER_ID", "")

            if (!userID.isNullOrEmpty()) {
                // Generate QR code using the userID
                val bitmap = generateQRCode(userID)
                qrImageView.setImageBitmap(bitmap)
            } else {
                // Handle scenario when userID is not available
                // You might prompt the user to login or take appropriate action
            }
        }
    }

    private fun generateQRCode(userID: String): Bitmap? {
        val qrCodeWriter = QRCodeWriter()
        try {
            val bitMatrix = qrCodeWriter.encode(userID, BarcodeFormat.QR_CODE, 500, 500)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(
                        x, y,
                        if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
                    )
                }
            }
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }
}
