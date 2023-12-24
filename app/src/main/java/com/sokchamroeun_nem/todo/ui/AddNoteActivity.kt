package com.sokchamroeun_nem.todo.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.sokchamroeun_nem.todo.databinding.ActivityAddNoteBinding
import com.sokchamroeun_nem.todo.db.NoteEntity
import com.sokchamroeun_nem.todo.utils.Constants
import com.sokchamroeun_nem.todo.utils.Constants.KEY
import com.sokchamroeun_nem.todo.utils.Constants.NEW
import com.sokchamroeun_nem.todo.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var entity: NoteEntity

    var notesList: List<String> = emptyList()

    private var noteId = 0
    private var complete = false
    private var name = ""
    private var timestamp = System.currentTimeMillis()

    private var type = ""
    private var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val type = intent.getStringExtra(KEY)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        // Display application icon in the toolbar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        if (type.equals(NEW)) {
            supportActionBar!!.title = "Add Note"
        } else {
            supportActionBar!!.title = "Update Note"
        }

        viewModel.getAllNotes()
        viewModel.notesList.observe(this@AddNoteActivity) { response ->
            if (!response.data.isNullOrEmpty()) {
                notesList = response.data.map { it.content }
            }
        }

        checkNewOrEditNote(type!!)
        saveNote()
    }

    private fun checkNewOrEditNote(type: String) {
        if (type.equals(NEW)) {
            isEdit = false
            binding.btnSave.text = "Save"
        } else {
            val noteEntity = intent.getParcelableExtra<NoteEntity>(Constants.ENTITY_ITEM)
            if (noteEntity != null) {
                noteId = noteEntity.id
                entity.content = noteEntity.content
                entity.timestamp = noteEntity.timestamp
                complete = noteEntity.completed

                isEdit = true
                binding.edtContent.setText(noteEntity.content)
                binding.btnSave.text = "Update"
            }
        }
    }

    private fun saveNote() {
        binding.btnSave.setOnClickListener {
            val content = binding.edtContent.text.toString().trim()
            if (content.isNotEmpty()) {
                val contentExists = notesList.any { it == content }
                if (contentExists) {
                    showNotificationDisabledWarning(this)
                    return@setOnClickListener
                }
            }

            if (content.isEmpty()) {
                Snackbar.make(it, "Content cannot be Empty!", Snackbar.LENGTH_SHORT).show()
            } else {
                entity.id = noteId
                entity.content = content
                entity.timestamp = timestamp
                entity.completed = complete

                viewModel.saveNote(isEdit, entity)

                binding.edtContent.setText("")
            }
            if (isEdit) {
                Toast.makeText(this, "Update Success", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun showNotificationDisabledWarning(context: Context) {
        MaterialAlertDialogBuilder(context).setTitle("Warning!")
            .setMessage("Not allow duplicate item in the list.")
            .setPositiveButton("Yes") { d, _ -> d.dismiss() }.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}