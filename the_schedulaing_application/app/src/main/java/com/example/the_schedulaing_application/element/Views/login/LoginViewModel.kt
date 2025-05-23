package com.example.the_schedulaing_application.element.Views.login

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.the_schedulaing_application.data.fb.UiUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val uiUserData: UiUserData
): ViewModel() {

    fun setUserName(username: String){
        uiUserData.setUserName(username)
    }
    fun setUserProfilePicture(img: Uri){
        uiUserData.setUserProfilePic(img)
    }

}