package com.example.the_schedulaing_application.data.fb

import android.content.Context
import android.net.Uri
import androidx.credentials.GetCredentialResponse
import androidx.credentials.CredentialManager
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.the_schedulaing_application.R
import com.example.the_schedulaing_application.domain.loginErrorHandling.LoginErrorHandler
import com.example.the_schedulaing_application.domain.loginErrorHandling.LoginHandles
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await

class GoogleSignInClient(
    private val context: Context,
){

    val loginErrorHandler = LoginErrorHandler()

    private val credentialManager = CredentialManager.create(context)
    private val firebaseAuth = FirebaseAuth.getInstance()

    private var _isSignedIn = MutableStateFlow(firebaseAuth.currentUser != null)
    val isSignedIn = _isSignedIn.asStateFlow()

    private var _newUser = MutableStateFlow(false)
    val newUser = _newUser.asStateFlow()

    fun getDisplayName():String{
        return firebaseAuth.currentUser?.displayName ?: "Not Signed In"
    }

    fun getProfilePicture(): Uri? {
        return firebaseAuth.currentUser?.photoUrl
    }

    private val _username = MutableStateFlow(getDisplayName())
    val username = _username.asStateFlow()

    private val _profilePicture = MutableStateFlow(getProfilePicture())
    val profilePicture = _profilePicture.asStateFlow()

    fun setUserName(name: String){
        try{
            firebaseAuth.currentUser?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            )?.addOnCompleteListener {
                println("Changed username")
                _username.update { firebaseAuth.currentUser?.displayName ?: "" }
            }
        }catch (e: Exception){
            println("Failed to update username - ${e.message}")
        }
    }

    fun setUserProfile(uri: Uri){
        try{
            firebaseAuth.currentUser?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build()
            )?.addOnCompleteListener {
                println("Changed profile picture")
                _profilePicture.update { firebaseAuth.currentUser?.photoUrl ?: Uri.EMPTY }
            }
        }catch (e: Exception){
            println("Failed to update profile picture - ${e.message}")
        }

    }

    suspend fun signIn(): Boolean {
        if (_isSignedIn.value) {
            println(" Already Signed In.")
            return true
        }
        try {

            val result = getCredentialResponse()
            return handleSignIn(result)

        }catch (e: Exception){
            println("Error signing in - ${e.message}")
            e.printStackTrace()
            if(e is CancellationException) throw e
            return false
        }
    }

    private suspend fun handleSignIn(result: GetCredentialResponse): Boolean{
        val credential = result.credential

        if(
            credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ){
            try {
                val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                val authCredential = GoogleAuthProvider.getCredential(tokenCredential.idToken,null)

                val authResult = firebaseAuth.signInWithCredential(authCredential).await()


                if(authResult.user != null) {
                    println("signed in!!")
                    _username.update { firebaseAuth.currentUser?.displayName ?: "" }
                    _profilePicture.update { firebaseAuth.currentUser?.photoUrl ?: Uri.EMPTY }
                    _isSignedIn.update { true }
                    return true
                }else{
                    _isSignedIn.update { false }
                    return false
                }

            }catch (e: Exception){
                println("Error handling - ${e.message}")
                return false
            }
        }else{
            println(" credential is not GoogleIdTokenCredential")
            return false
        }

    }

    private suspend fun getCredentialResponse(): GetCredentialResponse{
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_auth_key))
                    .setAutoSelectEnabled(false)
                    .build()
            ).build()

        return credentialManager.getCredential(context = context, request = request)
    }

    suspend fun signOut(){
        // Fix Sign out and in
        try{
            firebaseAuth.signOut()
            credentialManager.clearCredentialState(
                ClearCredentialStateRequest()
            )
            _isSignedIn.update { false }
        }catch (e: Exception){
            println("Error Signing Out - ${e.cause} ${e.message}")
        }

    }

    // With Email
    fun signUpWithEmail(email: String, password: String){

        if(email.isEmpty() || password.isEmpty() ){
            if(email.isEmpty()){
                loginErrorHandler.onError(LoginHandles.EmptyEmail)
            }
            if(password.isEmpty()){
                loginErrorHandler.onError(LoginHandles.EmptyPassword)
            }
            return

        }
        loginErrorHandler.onReset()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _newUser.update { true }
                    _isSignedIn.update { true }
                }else{
                    if(task.exception is FirebaseAuthInvalidCredentialsException){
                        loginErrorHandler.onError(LoginHandles.InvalidEmail)

                    }
                    if(task.exception is FirebaseAuthUserCollisionException){
                        loginErrorHandler.onError(LoginHandles.EmailCollision)
                    }
                    if(task.exception is FirebaseAuthWeakPasswordException){
                        loginErrorHandler.onError(LoginHandles.WeakPassword)
                    }
                    println("Signup Failed - ${task.exception} ")
                }
            }
            .addOnFailureListener {e ->
                if(e is FirebaseNetworkException){
                    loginErrorHandler.onError(LoginHandles.NetworkUnavailable)
                }

            }

    }

    fun signInWithEmail(email: String, password: String) {
        if(email.isEmpty() || password.isEmpty() ){
            if(email.isEmpty()){
                loginErrorHandler.onError(LoginHandles.EmptyEmail)
            }
            if(password.isEmpty()){
                loginErrorHandler.onError(LoginHandles.EmptyPassword)
            }
            return

        }
        loginErrorHandler.onReset()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isSignedIn.update { true }
                    _username.update { firebaseAuth.currentUser?.displayName ?: "" }
                    _profilePicture.update { firebaseAuth.currentUser?.photoUrl ?: Uri.EMPTY }
                } else {
                    if(task.exception is FirebaseAuthInvalidCredentialsException){
                        loginErrorHandler.onError(LoginHandles.InvalidPassword)
                    }
                    println("login failed -  ${task.exception}")
                }
            }
            .addOnFailureListener { e ->
                println("Failed to SignIn - ${e.message}")
            }


    }

    fun newUserSetup(){
        _newUser.update { false }
    }


}

data class UserData(
    val name: String,
    val profilePicture: Uri?
)