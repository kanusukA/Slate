package com.example.the_schedulaing_application.element.Views.calendar.dateDetail

import androidx.lifecycle.ViewModel
import com.example.the_schedulaing_application.element.Navigation.NavConductorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DateDetailViewModel @Inject constructor(
    private val navConductorViewModel: NavConductorViewModel
): ViewModel() {


    fun selectDate(date: Int){
        navConductorViewModel.selectedDate(date)
    }


}