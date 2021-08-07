package com.example.ussdproject.ui.navigation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ussdproject.ui.forward.Forward
import com.example.ussdproject.ui.forward.ForwardViewModel
import com.example.ussdproject.ui.intro.Intro
import kotlinx.coroutines.delay
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.arad.ussdlibrary.USSDController
import com.example.ussdproject.R
import com.example.ussdproject.common.Constant.input_number
import com.example.ussdproject.ui.forward.modify
import com.example.ussdproject.util.checkForPermissions
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.launch


@ExperimentalAnimationApi
@Composable
fun Nav_graph() {
    val navController = rememberAnimatedNavController()
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current
    AnimatedNavHost(navController = navController, startDestination = NavScreen.Intro.route) {
        composable(NavScreen.Intro.route,
        enterTransition = { initial, _ ->
            when (initial.destination.route) {
                NavScreen.Forward.route ->
                    slideInHorizontally(
                        initialOffsetX = { 700 },
                        animationSpec = tween(700)
                    ) + fadeIn(animationSpec = tween(700))
                else -> null
            }


        }){
            Intro()
            scope.launch {
                delay(3000)
                navController.navigate(NavScreen.Forward.route) {
                    popUpTo(NavScreen.Intro.route){ inclusive = true }
                    launchSingleTop = true
                }
            }
        }
        composable(NavScreen.Forward.route,
            exitTransition = { _, target ->
                when (target.destination.route) {
                    NavScreen.Intro.route ->
                        slideOutHorizontally(
                            targetOffsetX = { 300 },
                            animationSpec = tween(300)
                        ) + fadeOut(animationSpec = tween(300))
                    else -> null
                }
            }) {
            val (text, SetText) = remember { mutableStateOf("") }
            var enable by remember { mutableStateOf(true) }
            val viewModel = hiltViewModel<ForwardViewModel>()
            val enableButton by viewModel.enable.observeAsState()
            enableButton.let { enable = it!! }
            val dialog by remember {
                mutableStateOf(false)
            }

            Surface(color = MaterialTheme.colors.background) {
                Forward(
                    viewModel = viewModel, text = text, onTextChange = SetText,
                    ussdCall = {
                        checkAccesses(ctx = ctx, permission = {
                            if (text.trim() != "") {
                                scope.launch { viewModel.userIntent.send(ForwardViewModel.MainIntent.ForWardIntent(text))}
                            } else {
                                Toast.makeText(it,input_number, Toast.LENGTH_SHORT).show() }
                        })
                    },
                    disableCall = {
                        checkAccesses( ctx = ctx,permission = {
                            scope.launch {
                                viewModel.userIntent.send(ForwardViewModel.MainIntent.DisableIntent)

                            }
                        })
                    },

                    enable
                )

            }
        }

    }


}


fun checkAccesses( ctx: Context,permission: (ctx:Context) -> Unit) {
    if (checkForPermissions(ctx) && USSDController.verifyAccesibilityAccess(
            ctx
        )
    ) {
        permission(ctx)
    }
}





sealed class NavScreen(val route: String) {
    object Forward : NavScreen("ScreenForward")
    object Intro : NavScreen("ScreenIntro")
}


