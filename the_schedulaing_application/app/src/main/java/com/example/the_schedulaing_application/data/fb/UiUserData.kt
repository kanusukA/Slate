package com.example.the_schedulaing_application.data.fb

import android.net.Uri
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UiUserData {

    private val _userProfilePicture = MutableStateFlow(Uri.EMPTY)
    val userProfilePicture = _userProfilePicture.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    fun setUserProfilePic(img: Uri?){
        _userProfilePicture.update { img }
    }
    fun setUserName(username: String){
        _userName.update { username }
    }

}