package com.example.materialandroid.view.uselessNotes

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.materialandroid.R
import com.example.materialandroid.databinding.UselessNotesFragmentBinding
import com.example.materialandroid.viewModel.adapters.UselessNotesAdapter
import com.example.materialandroid.viewModel.interfaces.ItemTouchHelperCallback
import com.example.materialandroid.viewModel.interfaces.OnUselessListItemClickListener
import com.example.materialandroid.viewModel.uselessNotesModel.UselessNote
import java.util.*

class UselessNotesFragment : Fragment(R.layout.useless_notes_fragment) {

    companion object {
        fun newInstance(): UselessNotesFragment {
            val args = Bundle()

            val fragment = UselessNotesFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: UselessNotesFragmentBinding? = null
    private val binding: UselessNotesFragmentBinding
        get() {
            return _binding!!
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UselessNotesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notesList = mutableListOf(
            UselessNote(
                0,
                "Один 122",
                "В заметках работает сортировка и поиск заметок по словам",
                Date(Date().time),
                true
            ),
            UselessNote(1, "Два", "Разные слова для поиска по содержанию", Date(Date().time), true),
            UselessNote(2, "Три", getString(R.string.lorem), Date(Date().time), false),
            UselessNote(3, "Четыре", getString(R.string.lorem), Date(Date().time), false),
            UselessNote(4, "Пять 122", getString(R.string.lorem), Date(Date().time), true),
            UselessNote(5, "Шесть", getString(R.string.lorem), Date(Date().time), false),
            UselessNote(6, "Семь", getString(R.string.lorem), Date(Date().time), false),
            UselessNote(7, "Восемь", getString(R.string.lorem), Date(Date().time), false),
            UselessNote(8, "Девять 122", getString(R.string.lorem), Date(Date().time), false),
            UselessNote(9, "Десять", getString(R.string.lorem), Date(Date().time), true),
            UselessNote(10, "Одинадцать", getString(R.string.lorem), Date(Date().time), false),
            UselessNote(11, "Двенадцать", getString(R.string.lorem), Date(Date().time), false),
            UselessNote(12, "Тринадцать", getString(R.string.lorem), Date(Date().time), true),
            UselessNote(13, "Четырнадцать", getString(R.string.lorem), Date(Date().time), false),
            UselessNote(14, "Пятнадцать", getString(R.string.lorem), Date(Date().time), false),
        )

        val adapter = UselessNotesAdapter(object : OnUselessListItemClickListener {
            override fun onItemClick(note: UselessNote, position: Int) {
                val editDialog = EditUselessNotesDialogFragment(note)
                editDialog.show(requireActivity().supportFragmentManager, "editDialog")
                editDialog.setFragmentResultListener(getString(R.string.DIALOG_KEY)) { _, bundle ->
                    val newNote = bundle.getParcelable<UselessNote>(getString(R.string.RESULT_KEY))
                    if (newNote != null) {
                        notesList[position] = newNote
                    }
                    binding.recyclerView.adapter?.notifyItemChanged(position)

                }
            }
        }, notesList)

        binding.recyclerView.adapter = adapter

        binding.notesAddFAB.setOnClickListener {
            val editDialog = EditUselessNotesDialogFragment(UselessNote())
            editDialog.show(requireActivity().supportFragmentManager, "editDialog")
            editDialog.setFragmentResultListener(getString(R.string.DIALOG_KEY)) { _, bundle ->
                val newNote = bundle.getParcelable<UselessNote>(getString(R.string.RESULT_KEY))
                if (newNote != null) {
                    notesList.add(newNote)
                }
                binding.recyclerView.adapter?.notifyItemInserted(notesList.size - 1)
            }
        }

        val toolbar: Toolbar = binding.notesToolbar

        toolbar.inflateMenu(R.menu.notes_filters)
        toolbar.overflowIcon = requireActivity().getDrawable(R.drawable.ic_filter)

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.notesSortByDate -> {
                    notesList.sortByDescending { it.date }
                    adapter.notifyDataSetChanged()
                }

                R.id.notesSortByImportant -> {
                    notesList.sortByDescending { it.important }
                    adapter.notifyDataSetChanged()
                }

                R.id.notesSortByWord -> {
                    var mText: String

                    val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Введите искомое слово")


                    val input = EditText(requireContext())
                    input.hint = "Введите слово"
                    input.inputType = InputType.TYPE_CLASS_TEXT
                    builder.setView(input)

                    builder.setPositiveButton("OK") { _, _ ->

                        mText = input.text.toString()

                        adapter.setData(notesList.filter {
                            it.title.contains(
                                mText,
                                true
                            ) or it.description.contains(mText, true)
                        }
                            .toMutableList())
                        adapter.notifyDataSetChanged()

                    }

                    builder.setNegativeButton("Отмена") { dialog, _ -> dialog.cancel() }

                    builder.show()

                }

                R.id.notesReset -> {
                    adapter.setData(notesList)
                    adapter.notifyDataSetChanged()
                }

            }

            true
        }

        ItemTouchHelper(ItemTouchHelperCallback(adapter)).attachToRecyclerView(binding.recyclerView)


    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}