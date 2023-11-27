package com.alsam.mdbook_01

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class NewBodyLocationView : AppCompatActivity() {

    private lateinit var bodyFrontImageView: ImageView
    private lateinit var bodyBackImageView: ImageView
    private lateinit var doneButton: Button
    private lateinit var cancelButton: Button
    private lateinit var bodyPartEditText: EditText
    private lateinit var labelEditText: EditText

var id =""
    var frontUrl ="";
    var  backUrl="";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_body_location_view)
        val intent = intent

        // Retrieve data from the intent
        id = intent.getStringExtra("id").toString()

        frontUrl = intent.getStringExtra("frontPic").toString();
        backUrl = intent.getStringExtra("backPic").toString();
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
            intent.putExtra("frontPic",frontUrl);
            intent.putExtra("backPic",backUrl);

            startActivity(intent)
                finish()
        }

        bodyBackImageView.setOnClickListener {
            val intent = Intent(this@NewBodyLocationView, ChooseUploadActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("front","0")
            intent.putExtra("back","1")
            intent.putExtra("frontPic",frontUrl);
            intent.putExtra("backPic",backUrl);
            startActivity(intent)
            finish()

        }

        try {
            frontUrl =    intent.getStringExtra("frontPic").toString();
        } catch (e: Exception) {

        }
        try {
            backUrl =    intent.getStringExtra("backPic").toString();
        } catch (e: Exception) {
        }

        if(frontUrl.isNotEmpty())
        {

            Glide.with(this)
                .load(frontUrl)
                // image url
          // any placeholder to load at start
                // any image in case of error
             // resizing


                .centerCrop()
                .error(R.drawable.body_cutout_front)
                .into(bodyFrontImageView)



        }
         if(backUrl.isNotEmpty())
         {


             Glide.with(this)
                 .load(backUrl) // image url
                 // any placeholder to load at start
                 // any image in case of error
                 // resizing
                 .error(R.drawable.body_cutout_back)
                 .centerCrop()
                 .into(bodyBackImageView)

         }


        doneButton.setOnClickListener {

            val label = labelEditText.text.toString()
            val bodypart = bodyPartEditText.text.toString()

// Reference to the Firestore collection
            val collectionRef = FirebaseFirestore.getInstance().collection("records")

// Create a query to find the document where the field 'name' is equal to 'u'
            val query = collectionRef.whereEqualTo("id", id)

            query.get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        // Assuming there's only one document that matches the condition (name = 'u')
                        val documentId = documents.documents[0].id

                        // Reference to the specific document
                        val documentRef = collectionRef.document(documentId)

                        // Create a map to update the document
                        val updates = hashMapOf<String, Any>(
                            "label" to label,
                            "bodypart" to bodypart
                            // Add any other fields you want to update here
                        )

                        // Update the document
                        documentRef.update(updates)
                            .addOnSuccessListener {
                                // Document updated successfully
                                // You can perform additional actions here if needed
                                finish() // Finish the current activity
                            }
                            .addOnFailureListener { e ->
                                // Handle failures
                                // Log the error or perform any other actions
                            }
                    } else {
                        // No document found that matches the condition
                        // Handle accordingly
                    }
                }
                .addOnFailureListener { e ->
                    // Handle failures
                    // Log the error or perform any other actions
                }
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
