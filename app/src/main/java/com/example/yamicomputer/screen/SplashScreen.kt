package com.example.yamicomputer.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.yamicomputer.R
import com.example.yamicomputer.logic.SharedViewModel
import com.example.yamicomputer.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier
                .height(150.dp)
                .width(200.dp),
            painter = painterResource(id = R.drawable.yamilogo),
            contentDescription = "welcome to yami computer",
            contentScale = ContentScale.Fit
        )

        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = "YAMI Computer",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        CircularProgressIndicator(modifier = Modifier.padding(top = 50.dp))

    }

    LaunchedEffect(key1 = Unit) {

        delay(2000)
        try {

            if (sharedViewModel.isProfileComplete()) {
                navController.navigate(Routes.HomeScreen.id)
            } else if (sharedViewModel.isUserLoggedIn()) {
                navController.navigate(Routes.ProfileCreateScreen.id)
            } else {
                navController.navigate(Routes.LoginScreen.id)
            }

        } catch (_: Exception) {

        }

    }

}