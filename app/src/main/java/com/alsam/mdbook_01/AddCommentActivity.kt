package com.alsam.mdbook_01

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AddCommentActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val recordsCollection = db.collection("comments")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comment)

        val etAddComment: EditText = findViewById(R.id.etAddComment)
        val addCommentBtn: Button = findViewById(R.id.addCommentBtn)

        addCommentBtn.setOnClickListener {
            val commentText = etAddComment.text.toString().trim()

            if (commentText.isNotEmpty()) {
                val recordData = hashMapOf(
                    "comment" to commentText
                )

                recordsCollection.add(recordData)
                    .addOnSuccessListener { documentReference ->
                        val returnIntent = Intent()
                        returnIntent.putExtra("COMMENT_TEXT", commentText)
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        showMessage("Error adding record: $e")
                    }
            } else {
                showMessage("Please fill in both headline and description.")
            }
        }
    }

    private fun showMessage(message: String) {
        // Implement your message display logic (e.g., Toast) here
    }
}
