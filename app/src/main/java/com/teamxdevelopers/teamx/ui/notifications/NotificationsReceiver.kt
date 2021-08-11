package com.teamxdevelopers.teamx.ui.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.teamxdevelopers.teamx.database.Database
import com.teamxdevelopers.teamx.database.notifications.Notifications
import com.teamxdevelopers.teamx.ui.viewPost.ViewPostActivity
import com.teamxdevelopers.teamx.utils.NotificationsUtility
import com.teamxdevelopers.teamx.utils.loadBitmap
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class NotificationsReceiver:FirebaseMessagingService() {

    @Inject lateinit var manager:NotificationManager
    @Inject lateinit var notification:NotificationCompat.Builder
    @Inject lateinit var db:Database
    @Inject lateinit var notificationsUtility: NotificationsUtility

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        val data=Notifications.extractFromMap(p0.data)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel()

        if (!notificationsUtility.isEnabled())
            return

        notification
            .setContentTitle(data.title)
            .setContentIntent(getPendingIntent(data.postId))

        val randomId= Random.nextInt()

        db.notifications().add(data)

        data.thumbnail.loadBitmap{
            it?.let {
                notification.setStyle(NotificationCompat.BigPictureStyle().bigPicture(it))
            }
            manager.notify(randomId,notification.build())
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(){
        val channel=NotificationChannel("ntf","Notifications",NotificationManager.IMPORTANCE_HIGH).apply {
            description="Here you receive all your notifications"
        }
        manager.createNotificationChannel(channel)

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getPendingIntent(postId:String):PendingIntent{
        val intent=Intent(this, ViewPostActivity::class.java)
        intent.putExtra("postId",postId)

        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}