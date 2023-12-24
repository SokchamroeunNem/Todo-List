package com.sokchamroeun_nem.todo.di

import android.content.Context
import androidx.room.Room
import com.sokchamroeun_nem.todo.db.NoteDB
import com.sokchamroeun_nem.todo.db.NoteEntity
import com.sokchamroeun_nem.todo.utils.Constants.NOTE_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, NoteDB::class.java, NOTE_DATABASE
    ).allowMainThreadQueries()
        .fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideDao(db:NoteDB) = db.noteDao()

    @Provides
    fun provideEntity()=NoteEntity()

}