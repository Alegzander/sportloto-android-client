package com.vovasoft.sportloto.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vovasoft.sportloto.R

/***************************************************************************
 * Created by arseniy on 15/09/2017.
 ****************************************************************************/
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        remoteMessage?.also {
            if (remoteMessage.data.isNotEmpty()) {
                //TODO handle notifications
            }
        }
    }


    private fun notificationBuilder(messageBody: String): NotificationCompat.Builder {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "${getString(R.string.app_name)}_chanel_id"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "${getString(R.string.app_name)}_chanel_name"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(channelId, channelName, importance)
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        return NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
                .setAutoCancel(true)
                .setChannelId(channelId)
                .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_SOUND)
    }

}