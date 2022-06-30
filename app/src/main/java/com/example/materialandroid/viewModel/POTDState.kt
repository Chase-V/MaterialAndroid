package com.example.materialandroid.viewModel

import com.example.materialandroid.model.pictureOfTheDay.POTDResponseData

sealed class POTDState{
    data class Success(val pictureOfTheDayResponseData: POTDResponseData):POTDState()
    data class Loading(val progress: Int?):POTDState()
    data class Error(val error: Throwable):POTDState()
}
