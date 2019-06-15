package com.devtides.notes.ui


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.devtides.notes.R
import com.devtides.notes.room.Note
import com.devtides.notes.room.NoteDatabase
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.launch


class AddNoteFragment : BaseFragment() {

    private var note: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            note = AddNoteFragmentArgs.fromBundle(it).note
            titleET.setText(note?.title)
            bodyET.setText(note?.body)
        }

        buttonSave.setOnClickListener {
            val noteTitle = titleET.text.toString().trim()
            val noteBody = bodyET.text.toString().trim()

            if(noteTitle.isNullOrEmpty()) {
                titleET.error = "Title required"
                return@setOnClickListener
            }

            if(noteBody.isNullOrEmpty()) {
                bodyET.error = "Note body required"
                return@setOnClickListener
            }

            val newNote = Note(noteTitle, noteBody)

            launch {
                if(note == null) {
                    saveNote(newNote)
                } else {
                    updateNote(newNote)
                }
            }

            navigateBack()
        }
    }

    private suspend fun updateNote(newNote: Note) {
        newNote.id = note!!.id
        context?.let {
            NoteDatabase(it).getNodeDao().updateNote(newNote)
            Toast.makeText(it, "Note updated", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun saveNote(note: Note) {
        context?.let {
            NoteDatabase(it).getNodeDao().addNote(note)
            Toast.makeText(it, "Note created", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateBack() {
        val action = AddNoteFragmentDirections.actionSaveNote()
        Navigation.findNavController(buttonSave).navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.deleteNote -> {
                note?.let {
                    deleteNote()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteNote() {
        context?.let {
            AlertDialog.Builder(it).apply {
                setTitle("Are you sure?")
                setMessage("You cannot undo this operation.")
                setPositiveButton("OK") {dialog, which ->
                    launch {
                        NoteDatabase(it).getNodeDao().deleteNote(note!!)
                        navigateBack()
                    }
                }
                setNegativeButton("Cancel") {dialog, which ->  }
                show()
            }
        }
    }

}
