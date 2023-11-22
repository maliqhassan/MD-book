package com.alsam.mdbook_01

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AddRecordActivity : AppCompatActivity() {

    private val REQUEST_PICK_IMAGE = 102
    private lateinit var headlineEditText: EditText
    private lateinit var descriptionEditText: EditText
    private val db = FirebaseFirestore.getInstance()
    private val recordsCollection = db.collection("records")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_record)

        headlineEditText = findViewById(R.id.headline)
        descriptionEditText = findViewById(R.id.description)

        val addImageButton: ImageView = findViewById(R.id.addImage)
        val geoButton: Button = findViewById(R.id.geo)
        val bodyButton: Button = findViewById(R.id.body)
        val doneButton: Button = findViewById(R.id.done)
        val cancelButton: Button = findViewById(R.id.cancel)

        addImageButton.setOnClickListener {
            openGallery()
        }

        geoButton.setOnClickListener {
            val intent = Intent(this@AddRecordActivity, MapsActivity::class.java)
            startActivity(intent)
        }

        bodyButton.setOnClickListener {
            val intent = Intent(this@AddRecordActivity, NewBodyLocationView::class.java)
            startActivity(intent)
        }

        doneButton.setOnClickListener {
            val headline = headlineEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()

            if (headline.isNotEmpty() && description.isNotEmpty()) {
                val recordData = hashMapOf(
                    "headline" to headline,
                    "description" to description
                )

                recordsCollection.add(recordData)
                    .addOnSuccessListener { documentReference ->
                        showMessage("Record added successfully with ID: ${documentReference.id}")

                        val intent = Intent(this@AddRecordActivity, ItemRecord::class.java)
                        intent.putExtra("HEADLINE", headline)
                        intent.putExtra("DESCRIPTION", description)
                        startActivity(intent)

                        clearFields()
                    }
                    .addOnFailureListener { e ->
                        showMessage("Error adding record: $e")
                    }
            } else {
                showMessage("Please fill in both headline and description.")
            }
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearFields() {
        headlineEditText.text.clear()
        descriptionEditText.text.clear()
    }
}
