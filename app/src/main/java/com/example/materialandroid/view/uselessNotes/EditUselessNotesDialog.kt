package com.example.materialandroid.view.uselessNotes

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.materialandroid.R
import com.example.materialandroid.databinding.EditUselessNoteDialogFragmentBinding
import com.example.materialandroid.viewModel.uselessNotesModel.UselessNote
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

class EditUselessNotesDialogFragment(note: UselessNote) :
    DialogFragment(R.layout.edit_useless_note_dialog_fragment) {

    private var _binding: EditUselessNoteDialogFragmentBinding? = null
    private val binding: EditUselessNoteDialogFragmentBinding
        get() {
            return _binding!!
        }
    private val nnote = note

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EditUselessNoteDialogFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editNoteTitle.setText(nnote.title)
        binding.editNoteDescription.setText(nnote.description)
        binding.editNoteDate.text = (
                SimpleDateFormat(
                    "HH:mm dd/MM/yyyy",
                    Locale.getDefault()
                ).format(nnote.date)
                )
        binding.editNoteImportant.isChecked = nnote.important

        binding.editNoteButtonSave.setOnClickListener {

            val newNote = UselessNote(
                nnote.id,
                binding.editNoteTitle.text.toString(),
                binding.editNoteDescription.text.toString(),
                SimpleDateFormat("HH:mm dd/MM/yyyy").parse(
                    binding.editNoteDate.text.toString(), ParsePosition(0)
                ) as Date,
                binding.editNoteImportant.isChecked
            )

            setFragmentResult(
                getString(R.string.DIALOG_KEY),
                bundleOf(getString(R.string.RESULT_KEY) to newNote)
            )

            dismiss()
        }

        binding.editNoteDate.setOnClickListener { pickDateTime() }


    }

    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                TimePickerDialog(
                    requireContext(),
                    { _, hour, minute ->
                        val pickedDateTime = Calendar.getInstance()
                        pickedDateTime.set(year, month, day, hour, minute)
                        binding.editNoteDate.text = SimpleDateFormat(
                            "HH:mm dd/MM/yyyy",
                            Locale.getDefault()
                        ).format(pickedDateTime.time)
                    },
                    startHour,
                    startMinute,
                    false
                ).show()
            },
            startYear,
            startMonth,
            startDay
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}