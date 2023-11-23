package com.alsam.mdbook_01

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

import androidx.annotation.RequiresApi


/**
 * Required for proper reminder functionality. It builds a notification.
 *
 * @author Raj Kapadia
 * @author Vanessa Peng
 * @see AddReminderActivity
 */
class AlarmService : BroadcastReceiver() {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun onReceive(context: Context, intent: Intent) {
        val notificationID = intent.extras!!.getInt("notificationID")
        val mainIntent = Intent(context, AddReminderActivity::class.java)
        val contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = Notification.Builder(context)
        builder.setSmallIcon(R.drawable.iconlogo)
            .setContentTitle("Reminder!")
            .setContentText("Reminder to take pictures!")
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)
            .setContentIntent(contentIntent)

        notificationManager.notify(notificationID, builder.build())
    }
}

