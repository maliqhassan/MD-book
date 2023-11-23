package com.alsam.mdbook_01

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

class NewBodyLocationView : AppCompatActivity() {

    private lateinit var bodyFrontImageView: ImageView
    private lateinit var bodyBackImageView: ImageView
    private lateinit var doneButton: Button
    private lateinit var cancelButton: Button
    private lateinit var bodyPartEditText: EditText
    private lateinit var labelEditText: EditText

var id =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_body_location_view)
        val intent = intent

        // Retrieve data from the intent
        id = intent.getStringExtra("id").toString()
        bodyFrontImageView = findViewById(R.id.body_front)
        bodyBackImageView = findViewById(R.id.body_back)
        doneButton = findViewById(R.id.done)
        cancelButton = findViewById(R.id.cancel)
        bodyPartEditText = findViewById(R.id.bodypart)
        labelEditText = findViewById(R.id.label)

        bodyFrontImageView.setOnClickListener {
            val intent = Intent(this@NewBodyLocationView, ChooseUploadActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("front","1")
            intent.putExtra("back","0")

            startActivity(intent)
        }

        bodyBackImageView.setOnClickListener {
            val intent = Intent(this@NewBodyLocationView, ChooseUploadActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("front","0")
            intent.putExtra("back","1")
            startActivity(intent)
        }

        doneButton.setOnClickListener {
           finish()

        }

        cancelButton.setOnClickListener {

            finish() // Finish the activity or navigate back
        }
    }

    private fun saveBodyLocationData(bodyPart: String, label: String) {
        // Handle saving the body location data
        // For instance, you can save this data to a database or perform necessary operations
        // Replace this with actual code to save the data according to your application logic
    }
}
