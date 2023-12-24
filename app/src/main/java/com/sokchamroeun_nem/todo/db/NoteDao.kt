package com.sokchamroeun_nem.todo.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sokchamroeun_nem.todo.utils.Constants.NOTE_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNote(notesEntity: NoteEntity)

    @Update
    suspend fun updateNote(notesEntity: NoteEntity)

    @Delete
    suspend fun deleteNote(notesEntity: NoteEntity)

    @Query("SELECT * FROM $NOTE_TABLE WHERE id ==:id")
    fun getNote(id: Int): Flow<NoteEntity>

    @Query("SELECT * FROM $NOTE_TABLE")
    fun getAllNotes(): Flow<MutableList<NoteEntity>>

    @Query("DELETE FROM $NOTE_TABLE")
    fun deleteAllNotes()

    @Query("SELECT * FROM $NOTE_TABLE WHERE content LIKE '%' || :content || '%' ")
    fun searchNote(content: String): Flow<MutableList<NoteEntity>>

}