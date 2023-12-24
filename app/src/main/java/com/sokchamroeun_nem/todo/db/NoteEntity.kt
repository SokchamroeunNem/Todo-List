package com.sokchamroeun_nem.todo.db

import android.os.Parcelable
import android.widget.CheckBox
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sokchamroeun_nem.todo.utils.Constants.NOTE_TABLE
import kotlinx.parcelize.Parcelize

@Entity(tableName = NOTE_TABLE)
@Parcelize
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var content: String = "",
    var timestamp: Long = 0,
    var completed: Boolean = false
) : Parcelable