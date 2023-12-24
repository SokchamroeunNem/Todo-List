package com.sokchamroeun_nem.todo.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.sokchamroeun_nem.todo.R
import com.sokchamroeun_nem.todo.adapter.NotesAdapter
import com.sokchamroeun_nem.todo.databinding.ActivityMainBinding
import com.sokchamroeun_nem.todo.db.NoteEntity
import com.sokchamroeun_nem.todo.utils.Constants.KEY
import com.sokchamroeun_nem.todo.utils.Constants.NEW
import com.sokchamroeun_nem.todo.utils.DataStatus
import com.sokchamroeun_nem.todo.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NotesAdapter.OnNoteDeletedListener {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private lateinit var notesAdapter: NotesAdapter

    @Inject
    lateinit var entity: NoteEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // initialize the adapter
        notesAdapter = NotesAdapter(this)

        binding.btnAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            intent.putExtra(KEY, NEW)
            startActivity(intent)
        }

        observe()
    }

    private fun observe() {
        viewModel.getAllNotes()
        viewModel.notesList.observe(this@MainActivity) { response ->
            when (response) {
                is DataStatus.Success -> {
                    if (response.data?.isEmpty() == true) {
                        binding.listBody.visibility = View.GONE
                        binding.emptyBody.isVisible = true
                        binding.loading.isVisible = false
                    } else {
                        binding.listBody.visibility = View.VISIBLE
                        binding.emptyBody.isVisible = false

                        //bind the data to the ui
                        binding.loading.isVisible = false
                        notesAdapter.differ.submitList(response.data)
                        binding.rvNotes.apply {
                            layoutManager = LinearLayoutManager(this@MainActivity)
                            adapter = notesAdapter
                        }
                    }
                }

                is DataStatus.Error -> {
                    Toast.makeText(
                        this, response.message.toString(), Toast.LENGTH_SHORT
                    ).show()
                }

                is DataStatus.Loading -> {
                    binding.loading.isVisible = true
                    binding.emptyBody.isVisible = true
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        val search = menu.findItem(R.id.actionSearch)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.getSearchNotes(newText!!)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onNoteDeleted(note: NoteEntity) {
        viewModel.deleteContact(note)
        Toast.makeText(
            this, "Success", Toast.LENGTH_SHORT
        ).show()
    }

    override fun onNoteCheckBox(note: NoteEntity, status: Boolean) {
        entity.id = note.id
        entity.content = note.content
        entity.timestamp = note.timestamp
        entity.completed = status

        viewModel.saveNote(true, entity)
    }
}