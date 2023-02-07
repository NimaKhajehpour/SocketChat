package com.nima.socketchat.di

import android.content.Context
import androidx.room.Room
import com.nima.socketchat.database.MessageDao
import com.nima.socketchat.database.MessageDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideDao(database: MessageDatabase): MessageDao = database.dao()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MessageDatabase =
        Room.databaseBuilder(
            context,
            MessageDatabase::class.java,
            "MessageDatabase"
        ).fallbackToDestructiveMigration().build()
}