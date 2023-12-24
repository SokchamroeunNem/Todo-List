package com.sokchamroeun_nem.todo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sokchamroeun_nem.todo.db.NoteEntity
import com.sokchamroeun_nem.todo.repository.NoteRepository
import com.sokchamroeun_nem.todo.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {

    private var _notesList: MutableLiveData<DataStatus<List<NoteEntity>>> = MutableLiveData()
    val notesList: LiveData<DataStatus<List<NoteEntity>>> get() = _notesList

    init {
        getAllNotes()
    }

    fun saveNote(isEdite: Boolean, entity: NoteEntity) = viewModelScope.launch {
        if (isEdite) {
            repository.updateNote(entity)
        } else {
            repository.saveNote(entity)
        }
    }

    fun deleteContact(entity: NoteEntity) = viewModelScope.launch {
        repository.deleteNote(entity)
    }

    fun getAllNotes() = viewModelScope.launch {
        _notesList.postValue(DataStatus.Loading())
        repository.getAllNotes()
            .catch { _notesList.postValue(DataStatus.Error(it.message.toString())) }
            .collect { _notesList.postValue(DataStatus.Success(it)) }
    }

    fun getSearchNotes(name: String) = viewModelScope.launch {
        repository.searchNote(name).collect() {
            _notesList.postValue(DataStatus.Success(it))
        }
    }

}