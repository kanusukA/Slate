package com.example.the_schedulaing_application.element.Views.FunctionView

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.the_schedulaing_application.R
import com.example.the_schedulaing_application.ui.theme.LexendFamily
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme

@Composable
fun UserProfilePage(
    userName: String,
    userProfilePicture: Uri?,
    onSignOut: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateColorScheme.surfaceContainerLowest),
        contentAlignment = Alignment.Center
    ){

        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Image(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                painter = rememberAsyncImagePainter(
                userProfilePicture,
                error = painterResource(id = R.drawable.default_profile),
                placeholder = painterResource(id = R.drawable.default_profile)
            ),
                contentDescription = "Account Profile Picture"
            )

            Text(
                text = userName,
                fontFamily = LexendFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(78.dp))
            
            FilledTonalButton(onClick = {
                onSignOut()
            }) {
                Text(
                    text ="Sign Out",
                    fontFamily = LexendFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            }




        }
    }

}

@Preview
@Composable
fun PreviewUserProfilePage() {
    UserProfilePage(userName = "Rajat Kumar", userProfilePicture = null, onSignOut = {})
}