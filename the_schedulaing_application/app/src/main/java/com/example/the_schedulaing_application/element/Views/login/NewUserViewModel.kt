package com.example.the_schedulaing_application.element.Views.login

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.the_schedulaing_application.data.fb.UiUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NewUserViewModel @Inject constructor(
    private val uiUserData: UiUserData
) : ViewModel() {

    fun setUserName(username: String) {
        uiUserData.setUserName(username)
    }

    fun setProfilePicture(img: Uri) {
        uiUserData.setUserProfilePic(img)
    }

}