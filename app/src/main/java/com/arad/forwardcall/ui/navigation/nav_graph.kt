package com.arad.forwardcall.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import com.arad.forwardcall.ui.intro.Intro
import kotlinx.coroutines.delay
import com.arad.ussdlibrary.USSDApi
import com.arad.forwardcall.ui.forward.Forward
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.launch
import java.util.HashMap
import java.util.HashSet


@ExperimentalAnimationApi
@Composable
fun Nav_graph(map: HashMap<String, HashSet<String>>, ussdApi: USSDApi) {
    val navController = rememberAnimatedNavController()
    val scope = rememberCoroutineScope()




    AnimatedNavHost(navController = navController, startDestination = NavScreen.Intro.route) {
        composable(
            NavScreen.Intro.route,
            enterTransition = { initial, _ ->
                when (initial.destination.route) {
                    NavScreen.Forward.route ->
                        slideInHorizontally(
                            initialOffsetX = { 700 },
                            animationSpec = tween(700)
                        ) + fadeIn(animationSpec = tween(700))
                    else -> null
                }
            }) {
            Intro()
            scope.launch {
                delay(3000)
                navController.navigate(NavScreen.Forward.route) {
                    popUpTo(NavScreen.Intro.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }


        composable(
            NavScreen.Forward.route,
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
            Forward(map = map,ussdApi = ussdApi)
        }
    }


}




sealed class NavScreen(val route: String) {
    object Forward : NavScreen("ScreenForward")
    object Intro : NavScreen("ScreenIntro")
}


