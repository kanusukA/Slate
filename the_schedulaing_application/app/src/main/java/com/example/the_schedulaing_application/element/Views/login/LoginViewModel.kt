package com.example.the_schedulaing_application.element.Views.login

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.the_schedulaing_application.data.fb.GoogleSignInClient
import com.example.the_schedulaing_application.domain.loginErrorHandling.LoginHandles
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class LoginViewModel (
    private val googleAuthClient: GoogleSignInClient
): ViewModel() {

    val loginUiErrors = googleAuthClient.loginErrorHandler.loginErrors

    private val _emailError = MutableStateFlow(false)
    val emailError = _emailError.asStateFlow()

    val labelEmailText = loginUiErrors.map { errors ->
        var output = "E-Mail"
        errors.forEach {
            output = when(it){
                LoginHandles.EmailCollision -> "E-Mail in use"
                LoginHandles.EmailNotFound -> "Account Not Found"
                LoginHandles.EmptyEmail -> "E-Mail is Required"
                LoginHandles.IncorrectPasswordEmail -> "Incorrect E-Mail/Password"
                LoginHandles.InvalidEmail -> "Invalid E-Mail"
                else -> "E-Mail"
            }
        }
        _emailError.update { output != "E-Mail" }
        output
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = "E-Mail"
    )

    private val _passwordError = MutableStateFlow(false)
    val passwordError = _passwordError.asStateFlow()

    val labelPasswordText = loginUiErrors.map { errors ->
        var output = "Password"
        errors.forEach {
            output = when(it){
                LoginHandles.WeakPassword -> "Weak Password"
                LoginHandles.EmptyPassword -> "Password is Required"
                LoginHandles.IncorrectPasswordEmail -> "Incorrect E-Mail/Password"
                LoginHandles.InvalidPassword -> "Invalid Password"
                else -> "Password"
            }
        }
        _passwordError.update { output != "Password" }
        output
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = "Password"
    )

    private var signingIn = MutableStateFlow(false)

    val loginUiState: StateFlow<LoginUiStates> = googleAuthClient.isSignedIn.combine(signingIn) { googleState, signing ->
        if (googleState){
            signingIn.update { false }
            LoginUiStates.SIGNED_IN
        }else if(signing){
            println("Signing in...")
            LoginUiStates.SIGNING_IN
        }else{
            LoginUiStates.SIGNED_OUT
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = LoginUiStates.SIGNED_OUT
    )

    fun onClickSignInWithEmail(email: String, password: String){
        googleAuthClient.signInWithEmail(email, password)
    }

    fun onClickSignUpWithEmail(email: String, password: String){
        googleAuthClient.signUpWithEmail(email, password)
    }

    fun setUsername(username: String){
        googleAuthClient.setUserName(username)
    }

    fun setProfilePicture(profile: Uri){
        googleAuthClient.setUserProfile(profile)
    }

    fun newUsersSetup(){
        googleAuthClient.newUserSetup()
    }


    fun onClickSignIn(){
        signingIn.update { true }
        viewModelScope.launch {
            signingIn.update {googleAuthClient.signIn()}
        }
    }

    fun onClickSignOut(){
        viewModelScope.launch {
            googleAuthClient.signOut()
        }
    }

}

enum class LoginUiStates{
    SIGNED_OUT,
    SIGNED_IN,
    SIGNING_IN
}