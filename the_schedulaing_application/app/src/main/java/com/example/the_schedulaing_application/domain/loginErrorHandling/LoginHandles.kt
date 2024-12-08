package com.example.the_schedulaing_application.domain.loginErrorHandling

sealed class LoginHandles {
    //Format
    data object InvalidEmail: LoginHandles()
    data object InvalidPassword: LoginHandles()
    data object EmptyEmail: LoginHandles()
    data object EmptyPassword: LoginHandles()
    //FromNetwork
    data object EmailNotFound: LoginHandles()
    data object EmailCollision: LoginHandles()
    data object IncorrectPasswordEmail: LoginHandles()
    data object WeakPassword: LoginHandles()
    data object NetworkUnavailable: LoginHandles()

}
