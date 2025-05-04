package com.example.the_schedulaing_application.element.Navigation

import android.net.Uri
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.the_schedulaing_application.R
import com.example.the_schedulaing_application.custom.ScaleIndication
import com.example.the_schedulaing_application.data.fb.GoogleSignInClient
import com.example.the_schedulaing_application.element.BottomControlBar.BottomNavControlBar
import com.example.the_schedulaing_application.element.TopSearchBar.SearchBarComponent
import com.example.the_schedulaing_application.element.Views.AddCreateView.AddEventView
import com.example.the_schedulaing_application.element.Views.FunctionView.UserProfilePage
import com.example.the_schedulaing_application.element.Views.calendar.CalenderView
import com.example.the_schedulaing_application.element.Views.calendar.dateDetail.DateDetails
import com.example.the_schedulaing_application.element.Views.homePage.HomePageView
import com.example.the_schedulaing_application.element.Views.login.GoogleAuthViewModel
import com.example.the_schedulaing_application.element.Views.login.LoginViewPage
import com.example.the_schedulaing_application.element.Views.login.NewUserView
import com.example.the_schedulaing_application.ui.theme.AccentColor
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme

@Composable
fun NavConductor(
    googleSignInClient: GoogleSignInClient
) {

    val googleAuthViewModel = GoogleAuthViewModel(googleSignInClient)

    val isSignedIn by googleSignInClient.isSignedIn.collectAsStateWithLifecycle()
    val newUserSignedIn by googleSignInClient.newUser.collectAsStateWithLifecycle()

    val username by googleSignInClient.username.collectAsStateWithLifecycle()
    val profilePicture by googleSignInClient.profilePicture.collectAsStateWithLifecycle()

    if (newUserSignedIn){
        NewUserView(
            onComplete = {usrname,img -> googleAuthViewModel.newUsersSetup(usrname,img)}
        )
    }
    else if (isSignedIn){
        NavigationalViewPage(
            username,
            profilePicture,
            onSignOut = {
                googleAuthViewModel.onClickSignOut()
            }
        )
    } else{
        LoginViewPage(googleAuthViewModel)
    }



}

@Composable
private fun NavigationalViewPage(
    username: String,
    profilePicture: Uri?,
    onSignOut: () -> Unit
){

    val navViewModel = hiltViewModel<NavViewModel>()

    val currentPage by navViewModel.currentPage.collectAsStateWithLifecycle()

    var showAddButton by remember {
        mutableStateOf(true)
    }

    var showSearchBar by remember {
        mutableStateOf(false)
    }

    // Navigation
    val navController = rememberNavController()

    LaunchedEffect(currentPage) {
        if(currentPage.route != navController.currentDestination?.route){
            navController.navigate(route = currentPage.route)
        }
    }

    BackHandler {
        if(navController.currentDestination?.route != NavRoutes.HomePage.route){
            navViewModel.changePage(navController.previousBackStackEntry?.destination?.route ?: "")
            navController.popBackStack()
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SlateColorScheme.surfaceContainerLowest)
    ) {

        CalenderView(navController = navController)

        NavHost(
            navController = navController,
            startDestination = NavRoutes.HomePage.route,
        ) {
            composable(route = NavRoutes.HomePage.route) {
                LaunchedEffect(key1 = true) {
                    showAddButton = true
                    showSearchBar = true
                }
                Surface(modifier = Modifier.fillMaxSize(),
                    color = SlateColorScheme.surfaceContainerLowest
                ) {
                    HomePageView()
                }

            }
            composable(route = NavRoutes.AddEditPage.route) {
                LaunchedEffect(key1 = true) {
                    showAddButton = false
                    showSearchBar = false
                }
                Surface(modifier = Modifier.fillMaxSize(),
                    color = SlateColorScheme.surfaceContainerLowest
                ) {
                    AddEventView(
                        modifier = Modifier.padding(top = 24.dp, start = 8.dp, end = 8.dp)
                    )
                }
            }
            composable(route = NavRoutes.CalendarPage.route) {
                LaunchedEffect(key1 = true) {
                    showAddButton = false
                }

            }
            composable(route = NavRoutes.CalendarDetailPage.route) {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = SlateColorScheme.surfaceContainerLowest
                ) {
                    DateDetails(
                        modifier = Modifier.padding(top = 100.dp)
                    )
                }
            }

            composable(route = NavRoutes.FunctionPage.route) {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = SlateColorScheme.surfaceContainerLowest
                ) {
                    UserProfilePage(
                        username = username,
                        profilePicture = profilePicture ?: Uri.EMPTY,
                        onSignOut = { onSignOut() }
                    )
                }
            }
        }


        AnimatedVisibility(
            modifier = Modifier,
            visible = showSearchBar,
            enter = fadeIn(),
            exit = fadeOut()
        ){
            SearchBarComponent(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
        }

        BottomNavControlBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            currentPage = currentPage
        )

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 100.dp, end = 12.dp),
            visible = showAddButton,
            enter = fadeIn(),
            exit = fadeOut()
        ) {

            Box(modifier = Modifier
                .size(52.dp)
                .shadow(8.dp, CircleShape)
                .background(color = SlateColorScheme.secondaryContainer, CircleShape)
                .clickable(
                    interactionSource = null,
                    indication = ScaleIndication
                ) {
                    navViewModel.changePage(NavRoutes.AddEditPage.route)
                },
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(id = R.drawable.plus_icon_24px),
                    contentDescription = ""
                )
            }


            /*FloatingActionButton(
                containerColor = SlateColorScheme.primaryContainer,
                onClick = {
                    navController.navigate(NavRoutes.AddEditPage.route)
                    navViewModel.changePage(NavRoutes.AddEditPage)
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.plus_icon_24px),
                    contentDescription = ""
                )
            }*/


        }

    }
}

sealed class NavRoutes(val route: String) {
    data object HomePage : NavRoutes("home_page")
    data object CalendarPage : NavRoutes("calendar_page")
    data object CalendarDetailPage: NavRoutes("calendar_detail_page")
    data object AddEditPage : NavRoutes("add_edit_page")
    data object FunctionPage : NavRoutes("function_page")
}

@Preview
@Composable
fun PreviewNavConductor() {
}