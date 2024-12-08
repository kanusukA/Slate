package com.example.the_schedulaing_application.element.Navigation

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    private val navConductorViewModel: NavConductorViewModel
): ViewModel() {

    val currentPage = navConductorViewModel.navPage

    fun changePage(page: String){
        navConductorViewModel.changeNavPage(page)
    }

}