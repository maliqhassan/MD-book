package com.alsam.mdbook_01

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddReminderActivity : AppCompatActivity() {

    private lateinit var timePicker: TimePicker
    private lateinit var addReminderButton: Button
    private val db = FirebaseFirestore.getInstance()
    private val remindersCollection = db.collection("reminders")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

        timePicker = findViewById(R.id.timePicker)
        addReminderButton = findViewById(R.id.addReminderButton)

        addReminderButton.setOnClickListener {
            setReminder()
        }
    }

    private fun setReminder() {
        val calendar = Calendar.getInstance()
        val hour: Int
        val minute: Int

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hour = timePicker.hour
            minute = timePicker.minute
        } else {
            hour = timePicker.currentHour
            minute = timePicker.currentMinute
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        val reminderTime = calendar.timeInMillis

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
      //  val intent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent)

        val reminderData = hashMapOf(
            "hour" to hour,
            "minute" to minute,
            "reminderTime" to reminderTime
        )

        remindersCollection.add(reminderData)
            .addOnSuccessListener { documentReference ->
                showMessage("Reminder added successfully with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                showMessage("Error adding reminder: $e")
            }

        Toast.makeText(this, "Reminder set for $hour:$minute", Toast.LENGTH_SHORT).show()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
