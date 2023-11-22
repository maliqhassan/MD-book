package com.alsam.mdbook_01

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PatientProblemRecord : AppCompatActivity() {

    private val ADD_COMMENT_REQUEST = 1 // Request code for starting AddCommentActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.patient_problem_record)

        val fabAddRecord = findViewById<FloatingActionButton>(R.id.fabAddRecord)

        fabAddRecord.setOnClickListener {
            val intent = Intent(this@PatientProblemRecord, AddCommentActivity::class.java)
            startActivityForResult(intent, ADD_COMMENT_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_COMMENT_REQUEST && resultCode == Activity.RESULT_OK) {
            val commentText = data?.getStringExtra("COMMENT_TEXT")

            commentText?.let {
                val formattedComment = "CareGiver Says: $it"

                val commentTextView = findViewById<TextView>(R.id.comment)
                val spannable = SpannableStringBuilder(formattedComment)

                // Set the relative size for "CareGiver Says:"
                spannable.setSpan(
                    RelativeSizeSpan(1.5f),
                    0,
                    "CareGiver Says:".length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                commentTextView.text = spannable
            }
        }
    }
}
