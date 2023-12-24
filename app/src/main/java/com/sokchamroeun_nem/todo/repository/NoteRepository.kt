package com.sokchamroeun_nem.todo.repository

import com.sokchamroeun_nem.todo.db.NoteDao
import com.sokchamroeun_nem.todo.db.NoteEntity
import javax.inject.Inject

class NoteRepository @Inject constructor(private val dao: NoteDao) {
    suspend fun saveNote(entity : NoteEntity)=dao.saveNote(entity)
    suspend fun updateNote(entity: NoteEntity)= dao.updateNote(entity)
    suspend fun deleteNote(entity : NoteEntity)=dao.deleteNote(entity)
    fun getAllNotes()=dao.getAllNotes()
    fun searchNote(name: String) = dao.searchNote(name)
}