package com.example.the_schedulaing_application.element.Views.FunctionView

import androidx.lifecycle.ViewModel
import com.example.the_schedulaing_application.element.Navigation.NavConductorViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FunctionsViewModel @Inject constructor(
    navConductorViewModel: NavConductorViewModel
):ViewModel() {

    val functionPage = navConductorViewModel.functionViewPage

}