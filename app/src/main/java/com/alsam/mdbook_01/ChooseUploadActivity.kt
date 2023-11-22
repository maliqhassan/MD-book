package com.alsam.mdbook_01

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ChooseUploadActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 101
    private val REQUEST_PICK_IMAGE = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_upload)

        val openGalleryButton: Button = findViewById(R.id.openGallery)
        val openCameraButton: Button = findViewById(R.id.openCamera)

        openGalleryButton.setOnClickListener {
            openGallery()
        }

        openCameraButton.setOnClickListener {
            openCamera()
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap?
                    Toast.makeText(this, "Photo OK", Toast.LENGTH_SHORT).show()
                    // Finish the activity after displaying the toast
                    finish()
                }
                REQUEST_PICK_IMAGE -> {
                    val selectedImage = data?.data
                    // Display a toast message when an image is selected from the gallery
                    Toast.makeText(this, "Photo OK", Toast.LENGTH_SHORT).show()
                    // Finish the activity after displaying the toast
                    finish()
                }
            }
        }
    }
}
