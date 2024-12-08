package com.example.the_schedulaing_application.element.TopSearchBar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.the_schedulaing_application.R
import com.example.the_schedulaing_application.custom.ScaleIndication
import com.example.the_schedulaing_application.ui.theme.LexendFamily
import com.example.the_schedulaing_application.ui.theme.SlateColorScheme

@Composable
fun SearchBarCalendarComponent(
    modifier: Modifier = Modifier
){
    val viewModel = hiltViewModel<SearchBarViewModel>()

    val textType by viewModel.navConductorViewModel.searchBarText.collectAsStateWithLifecycle()

    var showFilterBar by remember {
        mutableStateOf(false)
    }

    Box (modifier = modifier
        ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .background(
                    SlateColorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(24.dp)
                )
                .clip(RoundedCornerShape(24.dp))
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(start = 24.dp),
                text = textType.text,
                fontFamily = LexendFamily,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = SlateColorScheme.onSurface
            )

            Row(
                modifier = Modifier.background(
                    color = SlateColorScheme.secondaryContainer,
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(48.dp)
                        .clickable(
                            interactionSource = null,
                            indication = ScaleIndication
                        ) {


                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.next_icon_24px),
                        contentDescription = ""
                    )
                }

                VerticalDivider(modifier = Modifier.padding(vertical = 4.dp))

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(48.dp)
                        .clickable {
                            showFilterBar = !showFilterBar
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.filter_icon_24px),
                        contentDescription = ""
                    )
                }
            }

        }

    }
}

@Preview
@Composable
fun PreviewSearchBarCalendarComponent(){
    SearchBarCalendarComponent()
}