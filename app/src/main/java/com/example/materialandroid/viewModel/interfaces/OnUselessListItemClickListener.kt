package com.example.materialandroid.viewModel.interfaces

import com.example.materialandroid.viewModel.uselessNotesModel.UselessNote

interface OnUselessListItemClickListener {
    fun onItemClick(note: UselessNote, position: Int)
}