package com.teamxdevelopers.teamx.di

import android.content.Context
import androidx.room.Room
import com.teamxdevelopers.teamx.database.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @Provides
    @Singleton
    fun ktorClient()= HttpClient(Android){
        install(JsonFeature)
    }

    @Provides
    @Singleton
    fun database(@ApplicationContext context:Context)=
        Room.databaseBuilder(context, Database::class.java,"database").fallbackToDestructiveMigration().build()

}