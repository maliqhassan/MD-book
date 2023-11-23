package com.alsam.mdbook_01

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AddReminderActivity : AppCompatActivity(), View.OnClickListener {
    ;
    private val notificationID = 1
    private var timePicker: TimePicker? = null
    private var alarm: AlarmManager? = null

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)
      var   addReminder: Button = findViewById(R.id.addReminderButton)
       var    timePicker : TimePicker = findViewById(R.id.timePicker)

        addReminder.setOnClickListener(this)
    }

    /**
     * Takes care of clicking the addReminderButton.
     * @param v
     */
    override fun onClick(v: View) {
        val intent = Intent(this@AddReminderActivity, AlarmService::class.java)
        intent.putExtra("notificationId", notificationID)
        val alarmIntent = PendingIntent.getBroadcast(
            this@AddReminderActivity,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        alarm = getSystemService(ALARM_SERVICE) as AlarmManager
        if (v.id == R.id.addReminderButton) {
            val hour = timePicker!!.currentHour
            val minute = timePicker!!.currentMinute
            val startTime = Calendar.getInstance()
            startTime[Calendar.HOUR_OF_DAY] = hour
            startTime[Calendar.MINUTE] = minute
            startTime[Calendar.SECOND] = 0
            val alarmStartTime = startTime.timeInMillis
            alarm!!.setRepeating(
                AlarmManager.RTC_WAKEUP,
                alarmStartTime,
                AlarmManager.INTERVAL_DAY,
                alarmIntent
            )
            Toast.makeText(
                this@AddReminderActivity,
                timePicker!!.currentHour.toString() + timePicker!!.currentMinute.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}