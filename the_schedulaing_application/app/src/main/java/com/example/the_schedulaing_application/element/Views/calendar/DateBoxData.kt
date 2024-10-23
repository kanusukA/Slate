package com.example.the_schedulaing_application.element.Views.calendar

import com.example.the_schedulaing_application.domain.Cases.SlateEvent

data class DateBoxData (
    val date: Int,
    val events: List<SlateEvent>,
)