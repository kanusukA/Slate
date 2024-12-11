package com.example.the_schedulaing_application.element.Views.login

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.the_schedulaing_application.R
import com.example.the_schedulaing_application.custom.ScaleIndication
import com.example.the_schedulaing_application.element.components.sentientTextBox.SentientTextBox
import com.example.the_schedulaing_application.ui.theme.LexendFamily
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme

@Composable
fun NewUserView(
    onComplete: (username: String , image: Uri) -> Unit
) {

    val newUserViewModel = hiltViewModel<NewUserViewModel>()

    var username by remember {
        mutableStateOf("")
    }
    var imageUri by remember {
        mutableStateOf(Uri.EMPTY)
    }

    var usernameCheck by remember {
        mutableStateOf(false)
    }

    val usernameLabel by remember(usernameCheck) {
        mutableStateOf(
            if(usernameCheck){
                "Username Required"
            }else{
                "Username"
            }
        )
    }

    val profilePictureSelector =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                newUserViewModel.setProfilePicture(uri)
                imageUri = uri
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateColorScheme.surfaceContainerLowest)
    ) {

        Text(
            modifier = Modifier.padding(top = 32.dp, start = 6.dp),
            text = "Setup",
            fontFamily = LexendFamily,
            fontSize = 56.sp,
            fontWeight = FontWeight.Black,
            color = SlateColorScheme.onSurface
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Setup new user profile",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontFamily = LexendFamily,
                    fontWeight = FontWeight.Light
                )

                Spacer(modifier = Modifier.height(32.dp))

                Image(
                    modifier = Modifier
                        .size(144.dp)
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = null,
                            indication = ScaleIndication
                        ) {
                            profilePictureSelector.launch(PickVisualMediaRequest())
                        },
                    painter = rememberAsyncImagePainter(
                        model = imageUri,
                        placeholder = painterResource(id = R.drawable.default_profile),
                        error = painterResource(id = R.drawable.default_profile),
                        fallback = painterResource(id = R.drawable.default_profile)
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = ""
                )

                Spacer(modifier = Modifier.height(32.dp))

                SentientTextBox(textFieldValue = username,
                    labelText = usernameLabel,
                    indentHeight = 18.dp,
                    labelTextSize = 14,
                    onValueChanged = {
                        newUserViewModel.setUserName(it)
                        username = it
                    }
                )

                Spacer(modifier = Modifier.height(64.dp))

                FilledTonalButton(onClick = {
                    if(username.isEmpty()){
                        usernameCheck = true
                    }else{
                        newUserViewModel.setUserName(username)
                        onComplete(username , imageUri)
                    }
                }) {
                    Text(
                        text = "Done",
                        fontFamily = LexendFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }


            }
        }


    }
}

@Preview
@Composable
private fun NewUserViewPreview() {
    //NewUserView()
}