package com.example.the_schedulaing_application.element.Views.FunctionView

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.the_schedulaing_application.data.fb.UiUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val uiUserData: UiUserData
): ViewModel() {

}