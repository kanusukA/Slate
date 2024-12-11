package com.example.the_schedulaing_application.data.fb

import android.net.Uri
import com.example.the_schedulaing_application.data.viewModels.MainRealmViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class UiUserData (
    private val realmViewModel: MainRealmViewModel
){



    fun setUserProfilePic(img: Uri?){
       // realmViewModel.insertProfilePicture(img ?: Uri.EMPTY)
    }
    fun setUserName(username: String){
       // realmViewModel.insertUsername(username)
    }


}