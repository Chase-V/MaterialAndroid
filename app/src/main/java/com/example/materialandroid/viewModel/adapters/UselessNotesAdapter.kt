package com.example.materialandroid.viewModel.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.materialandroid.databinding.UselessNotesItemBinding
import com.example.materialandroid.viewModel.interfaces.ItemTouchHelperAdapter
import com.example.materialandroid.viewModel.interfaces.ItemTouchHelperViewHolder
import com.example.materialandroid.viewModel.interfaces.OnUselessListItemClickListener
import com.example.materialandroid.viewModel.uselessNotesModel.UselessNote
import java.text.SimpleDateFormat
import java.util.*

class UselessNotesAdapter(
    private var onUselessListItemClickListener: OnUselessListItemClickListener,
    private var notes: MutableList<UselessNote>
) : RecyclerView.Adapter<UselessNotesAdapter.UselessNoteViewHolder>(), ItemTouchHelperAdapter {

    fun setData(data: MutableList<UselessNote>) {
        notes = data
    }

    private lateinit var binding: UselessNotesItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UselessNoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = UselessNotesItemBinding.inflate(inflater, parent, false)

        return UselessNoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UselessNoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class UselessNoteViewHolder(private val noteBinding: UselessNotesItemBinding) :
        RecyclerView.ViewHolder(noteBinding.root), ItemTouchHelperViewHolder {

        fun bind(note: UselessNote) {

            itemView.setOnClickListener {
                onUselessListItemClickListener.onItemClick(
                    note,
                    adapterPosition
                )
            }

            noteBinding.noteImportant.isChecked = note.important
            noteBinding.noteDate.text =
                SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).format(note.date)
            noteBinding.noteTitle.text = note.title
            noteBinding.noteDescription.text = note.description

        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        notes.removeAt(fromPosition).apply {
            notes.add(
                if (toPosition > fromPosition) toPosition - 1
                else toPosition, this
            )
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        notes.removeAt(position)
        notifyItemRemoved(position)
    }
}