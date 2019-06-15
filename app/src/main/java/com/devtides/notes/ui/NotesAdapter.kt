package com.devtides.notes.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.devtides.notes.R
import com.devtides.notes.databinding.ItemNoteBinding
import com.devtides.notes.room.Note
import kotlinx.android.synthetic.main.item_note.view.*

class NotesAdapter(val notes: List<Note>): RecyclerView.Adapter<NotesAdapter.NoteViewHolder>(), NoteClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemNoteBinding>(
            inflater, R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.view.note = notes[position]
        holder.view.listener = this
//        holder.view.titleTV.text = notes[position].title
//        holder.view.bodyTV.text = notes[position].body
//        holder.view.root.setOnClickListener {
//            val action = HomeFragmentDirections.actionAddNote()
//            action.note = notes[position]
//            Navigation.findNavController(it).navigate(action)
//        }
    }

    override fun onNoteClicked(v: View) {
        val id = v.idTV.text.toString().toIntOrNull()
        var note: Note? = null
        for(n: Note in notes) {
            if (n.id == id) {
                note = n
                break
            }
        }

        note?.let {
            val action = HomeFragmentDirections.actionAddNote()
            action.note = note
            Navigation.findNavController(v).navigate(action)
        }
    }

    class NoteViewHolder(val view: ItemNoteBinding) : RecyclerView.ViewHolder(view.root)
}