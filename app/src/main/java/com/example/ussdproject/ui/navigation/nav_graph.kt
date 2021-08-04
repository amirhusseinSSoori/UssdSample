package com.example.ussdproject.ui.navigation

import android.util.Log
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ussdproject.ui.forward.Forward
import com.example.ussdproject.ui.forward.ForwardViewModel
import com.example.ussdproject.ui.intro.Intro
import kotlinx.coroutines.delay
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch



@Composable
fun Nav_graph() {
    val navController: NavHostController = rememberNavController()
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current
    NavHost(navController = navController, startDestination = NavScreen.Intro.route) {
        composable(NavScreen.Intro.route) {
            Intro()
            scope.launch {
                delay(3000)
                navController.navigate(NavScreen.Forward.route) {
                    popUpTo(NavScreen.Intro.route) { inclusive = true }
                }
            }

        }
        composable(NavScreen.Forward.route) {
            val (text, SetText) = remember { mutableStateOf("") }
            var enable by remember { mutableStateOf(true) }
            val viewModel = hiltViewModel<ForwardViewModel>()
            val enableButton by viewModel.enable.observeAsState()



            enableButton.let {

                Log.e("enableButton", "Nav_graph: ${it}", )
                enable = it!! }

            Surface(color = MaterialTheme.colors.background) {
                Forward(
                    viewModel = viewModel, text = text, onTextChange = SetText,
                    ussdCall = {
                        scope.launch {
                            viewModel.userIntent.send(ForwardViewModel.MainIntent.ForWardIntent(text))
                        }
                               },
                    disableCall = {
                        scope.launch {
                            viewModel.userIntent.send(ForwardViewModel.MainIntent.DisableIntent)
                        }
                    },
                    enable
                )

            }
        }

    }


}


sealed class NavScreen(val route: String) {
    object Forward : NavScreen("ScreenForward")
    object Intro : NavScreen("ScreenIntro")
}


