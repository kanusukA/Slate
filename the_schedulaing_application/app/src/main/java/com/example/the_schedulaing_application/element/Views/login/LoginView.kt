package com.example.the_schedulaing_application.element.Views.login

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.the_schedulaing_application.R
import com.example.the_schedulaing_application.custom.ScaleIndication
import com.example.the_schedulaing_application.element.components.sentientTextBox.SentientTextBox
import com.example.the_schedulaing_application.ui.theme.LexendFamily
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme

@Composable
fun LoginViewPage(
    loginViewModel: LoginViewModel
) {

    val loginState by loginViewModel.loginUiState.collectAsStateWithLifecycle()

    val loginEmailLabel by loginViewModel.labelEmailText.collectAsStateWithLifecycle()
    val loginPasswordLabel by loginViewModel.labelPasswordText.collectAsStateWithLifecycle()

    val _emailError by loginViewModel.emailError.collectAsStateWithLifecycle()
    val _passwordError by loginViewModel.passwordError.collectAsStateWithLifecycle()

    val emailError by remember(_emailError) {
        mutableStateOf(if (_emailError) {
            SlateColorScheme.onErrorContainer
        } else {
            SlateColorScheme.onSecondaryContainer
        })
    }
    val passwordError by remember(_passwordError) {
        mutableStateOf(if (_passwordError) {
            SlateColorScheme.onErrorContainer
        } else {
            SlateColorScheme.onSecondaryContainer
        })
    }


    var emailId by remember {
        mutableStateOf("")
    }

    var pass by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateColorScheme.surfaceContainerLowest)
    ) {

        Text(
            modifier = Modifier.padding(top = 32.dp, start = 6.dp),
            text = "Welcome",
            fontFamily = LexendFamily,
            fontSize = 56.sp,
            fontWeight = FontWeight.Black,
            color = SlateColorScheme.onSurface
        )

        Text(
            modifier = Modifier.padding(start = 6.dp),
            text = "Slate",
            fontFamily = LexendFamily,
            fontSize = 28.sp,
            fontWeight = FontWeight.Light,
            color = SlateColorScheme.onSurface
        )


        Box(
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    text = "Login with your Google account \n to continue",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontFamily = LexendFamily,
                    fontWeight = FontWeight.Light
                )

                Spacer(modifier = Modifier.height(32.dp))

                SentientTextBox(textFieldValue = emailId,
                    labelText = loginEmailLabel,
                    indentHeight = 18.dp,
                    labelTextSize = 14,
                    labelTextColor = emailError,
                    onValueChanged = { emailId = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SentientTextBox(textFieldValue = pass,
                    labelText = loginPasswordLabel,
                    indentHeight = 18.dp,
                    labelTextSize = 14,
                    visualTransformation = PasswordVisualTransformation(),
                    labelTextColor = passwordError,
                    onValueChanged = { pass = it }
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilledTonalButton(
                        onClick = {
                            if (
                                loginState == LoginUiStates.SIGNED_OUT
                            ) {
                                loginViewModel.onClickSignInWithEmail(emailId, pass)
                            }
                        }
                    ) {
                        Text(
                            text = when (loginState) {
                                LoginUiStates.SIGNED_OUT -> "Sign In"
                                LoginUiStates.SIGNED_IN -> "Sign Out"
                                LoginUiStates.SIGNING_IN -> "Signing In.."
                            },
                            fontFamily = LexendFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    FilledTonalButton(
                        onClick = {
                            if (
                                loginState == LoginUiStates.SIGNED_OUT
                            ) {
                                loginViewModel.onClickSignUpWithEmail(emailId, pass)
                            }
                        }
                    ) {
                        Text(
                            text = when (loginState) {
                                LoginUiStates.SIGNED_OUT -> "Sign Up"
                                LoginUiStates.SIGNING_IN -> "Signing Up.."
                                else -> "Sign Up"
                            },
                            fontFamily = LexendFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
            }
        }

        Image(
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.CenterHorizontally)
                .weight(0.18f)
                .clickable(
                    interactionSource = null,
                    indication = ScaleIndication
                ) {
                    loginViewModel.onClickSignIn()
                },
            painter = painterResource(id = R.drawable.google_icon),
            contentDescription = "Google login"
        )


    }

}

