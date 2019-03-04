package com.sbw.notofications

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.view.View
import android.widget.Button
import java.util.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var btnotifi_i: Button? = null
    private var notificationManager: NotificationManager? = null
    private var notifier_counter = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()


    }

    private fun init() {
        notificationManager =
                getSystemService(
                        Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationGroups()
        createNotificationChannels()

        btnotifi_i = findViewById(R.id.bt_notification_i) as Button

    }

    private fun createNotificationGroups() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val list = ArrayList<NotificationChannelGroup>()
            list.add(NotificationChannelGroup("home", "in"))
            list.add(NotificationChannelGroup("home", "out"))

            notificationManager?.createNotificationChannelGroups(list)

        }
    }

    private fun createNotificationChannels() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel("home", "in", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.group = "home"
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

            if (notificationManager != null) {
                notificationManager?.createNotificationChannel(notificationChannel)
//                notificationManager.createNotificationChannel(notificationChannel2)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        this.btnotifi_i?.setOnClickListener(this)
    }



    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.bt_notification_i ->{
                var contentIntent = PendingIntent.getActivity(this@MainActivity, 0, Intent(this@MainActivity, MainActivity::class.java), 0)


                var channel_id = ""
                var group_id = ""
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    group_id = "home"
                    channel_id = notificationManager?.getNotificationChannel("home").toString()
                    contentIntent = PendingIntent.getActivity(this@MainActivity, 0, Intent(this@MainActivity, MainActivity::class.java).putExtra("importance", notificationManager?.getNotificationChannel(channel_id)?.importance).putExtra("channel_id", channel_id), PendingIntent.FLAG_UPDATE_CURRENT)

                }

                val notification = NotificationCompat.Builder(this@MainActivity, channel_id)
                        .setContentTitle("My notification")
                        .setContentText("Much longer text that cannot fit one line...")
                        .setGroup(group_id)
                        .setContentIntent(contentIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)

                notifier_counter++

                notificationManager?.notify(notifier_counter, notification.build())

            }
        }
    }
}
