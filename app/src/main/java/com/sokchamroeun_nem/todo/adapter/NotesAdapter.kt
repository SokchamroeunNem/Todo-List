package com.sokchamroeun_nem.todo.adapter

import android.content.Intent
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sokchamroeun_nem.todo.databinding.ItemNoteBinding
import com.sokchamroeun_nem.todo.db.NoteEntity
import com.sokchamroeun_nem.todo.ui.AddNoteActivity
import com.sokchamroeun_nem.todo.utils.Constants
import com.sokchamroeun_nem.todo.utils.Utils

class NotesAdapter(private val deleteListener: OnNoteDeletedListener) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private lateinit var binding: ItemNoteBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        fun setData(item: NoteEntity) {
            binding.apply {
                tvContent.text = item.content
                binding.btnDelete.setOnClickListener {
                    deleteListener.onNoteDeleted(item)
                }
                binding.btnEdit.setOnClickListener {
                    val intent = Intent(it.context, AddNoteActivity::class.java)
                    intent.putExtra(Constants.KEY, Constants.EDIT)
                    intent.putExtra(Constants.ENTITY_ITEM, item) // item is NoteEntity instance
                    it.context.startActivity(intent)
                }



                Log.d("checkBox", "CheckBox: ${item.completed}")
                // Set checked
                if (!item.completed) {
                    Utils.removeChecked(binding.tvContent)
                } else {
                    Utils.setChecked(binding.tvContent)
                }

                binding.completedCheckBox.isChecked = item.completed

                completedCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                    deleteListener.onNoteCheckBox(item, isChecked)
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<NoteEntity>() {
        override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    interface OnNoteDeletedListener {
        fun onNoteDeleted(note: NoteEntity)
        fun onNoteCheckBox(note: NoteEntity, status: Boolean)
    }

    interface OnNoteCheckBoxListener {
        fun onNoteCheckBox(note: NoteEntity, status: Boolean)
    }

}