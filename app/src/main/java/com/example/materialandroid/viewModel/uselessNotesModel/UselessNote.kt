package com.example.materialandroid.viewModel.uselessNotesModel


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class UselessNote(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val date: Date = Date(Date().time),
    val important: Boolean = false
) : Parcelable