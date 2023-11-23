package com.alsam.mdbook_01

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class OptionMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option_menu)

        val intent = intent

        // Retrieve data from the intent
        val id = intent.getStringExtra("id")

        val setReminderBtn = findViewById<Button>(R.id.setRemindersBtn)
        val geoLocationBtn = findViewById<Button>(R.id.GeoLocation)
        val editRecordBtn = findViewById<Button>(R.id.EditRecord)
        var  viewRecorBtn = findViewById<Button>(R.id.EditProblem)

        viewRecorBtn.setOnClickListener {
            // Open AddReminderActivity
            val intent = Intent(this@OptionMenuActivity, EditProblemDetailsActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }

        setReminderBtn.setOnClickListener {
            // Open AddReminderActivity
            val intent = Intent(this@OptionMenuActivity, AddReminderActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }

        geoLocationBtn.setOnClickListener {
            // Open MapsActivity

            val intent = Intent(this@OptionMenuActivity, MapActivity::class.java)
            intent.putExtra("id",id)
            intent.putExtra("showData","true");

            startActivity(intent)
        }

        editRecordBtn.setOnClickListener {
            // Open AddRecordActivity

            val intent = Intent(this@OptionMenuActivity, ListRecordActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }
    }
}
