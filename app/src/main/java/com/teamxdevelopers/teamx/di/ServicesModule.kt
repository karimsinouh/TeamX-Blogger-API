package com.teamxdevelopers.teamx.di

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.teamxdevelopers.teamx.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ServiceComponent::class)
object ServicesModule {

    @Provides
    fun notificationManager(@ApplicationContext c:Context)=
        c.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    fun notification(@ApplicationContext c:Context)=
        NotificationCompat.Builder(c,"ntf")
            .setSmallIcon(R.drawable.ic_bookmark_outlined)
            .setAutoCancel(true)

}