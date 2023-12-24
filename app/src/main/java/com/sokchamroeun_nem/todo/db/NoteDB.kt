package com.sokchamroeun_nem.todo.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class NoteDB : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}