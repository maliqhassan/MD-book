package com.alsam.mdbook_01

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class OptionsMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option_menu) // Replace with your layout name


        val editProblemButton: Button = findViewById(R.id.EditProblem)
        val editRecordButton: Button = findViewById(R.id.EditRecord)
        val geoLocationButton: Button = findViewById(R.id.GeoLocation)
        val setRemindersButton: Button = findViewById(R.id.setRemindersBtn)

        editProblemButton.setOnClickListener {
            val intent = Intent(this@OptionsMenuActivity, AddRecordActivity::class.java)
            startActivity(intent)
        }

        editRecordButton.setOnClickListener {
            finish()
        }

        geoLocationButton.setOnClickListener {
            val intent = Intent(this@OptionsMenuActivity, MapActivity::class.java)
            startActivity(intent)
        }

        setRemindersButton.setOnClickListener {
            val intent = Intent(this@OptionsMenuActivity, AddReminderActivity::class.java)
            startActivity(intent)

        }
    }
}
