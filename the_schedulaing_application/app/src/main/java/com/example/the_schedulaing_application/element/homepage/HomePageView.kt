package com.example.the_schedulaing_application.element.homepage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.the_schedulaing_application.element.calendar.CalViewModel
import com.example.the_schedulaing_application.element.calendar.CalenderView

@Composable
fun HomePageView(){
    val calViewModel = remember {
        CalViewModel()
    }
    CalenderView(viewModel = calViewModel)
}

@Preview
@Composable
fun PreviewHomePageView(){
    HomePageView()
}