package com.example.the_schedulaing_application.domain.loginErrorHandling

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginErrorHandler {

    private val _loginErrors: MutableStateFlow<List<LoginHandles>> = MutableStateFlow(emptyList())
    val loginErrors = _loginErrors.asStateFlow()

    fun onError(error: LoginHandles){
        if(!_loginErrors.value.contains(error)){
            _loginErrors.value += error
            println("login added - ${loginErrors.value}")
        }
    }
    fun onReset(){
        _loginErrors.value = emptyList()
    }

}