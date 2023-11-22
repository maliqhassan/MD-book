package com.alsam.mdbook_01

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class ItemRecord : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record_item)

        val recordTitle: TextView = findViewById(R.id.recordTitle)
        val recordComments: TextView = findViewById(R.id.recordComments)

        val intent = intent
        val title = intent.getStringExtra("HEADLINE")
        val description = intent.getStringExtra("DESCRIPTION")

        recordTitle.text = title ?: "Your Title"
        recordComments.text = description ?: "Your Description"

        // Rest of the code...
    }

}
