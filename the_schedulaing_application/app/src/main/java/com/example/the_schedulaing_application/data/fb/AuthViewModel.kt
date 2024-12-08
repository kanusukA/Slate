package com.example.the_schedulaing_application.data.fb

import android.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel

class AuthViewModel(

): ViewModel() {

    suspend fun handleSignIn(result: GetCredentialResponse) {
        // API val credential = result
    }
}