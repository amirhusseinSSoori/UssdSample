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
import com.example.ussdproject.ui.intro.Intro
import kotlinx.coroutines.delay
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.arad.ussdlibrary.USSDApi
import com.arad.ussdlibrary.USSDController
import com.example.ussdproject.R
import com.example.ussdproject.common.Constant
import com.example.ussdproject.common.Constant.input_number
import com.example.ussdproject.ui.forward.*
import com.example.ussdproject.util.AlertDialogComponent
import com.example.ussdproject.util.alertShowDetails
import com.example.ussdproject.util.checkForPermissions
import com.example.ussdproject.util.modify
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
            var enableDialog by remember { mutableStateOf(false) }
            var textDialog by remember { mutableStateOf("") }




            Surface(color = MaterialTheme.colors.background) {
                if (enableDialog) {
                    AlertDialogComponent(openDialog = {
                        enableDialog = false
                    },textDialog)

                }



                Forward(
                    text = text, onTextChange = SetText,
                    ussdCall = {
                        checkAccesses(ctx = ctx, permission = {
                            if (text.trim() != "") {
                                enable = false
                                ussdApi.callUSSDInvoke(
                                    "*21*$text#",
                                    map,
                                    object : USSDController.CallbackInvoke {
                                        override fun responseInvoke(message: String) {
                                        }

                                        override fun over(message: String) {

//                                            alertShowDetails(ctx = ctx, modify(message))
                                            enableDialog = true
                                            textDialog = message
                                            enable = true

                                        }
                                    }, 0
                                )
                            } else {
                                Toast.makeText(it, input_number, Toast.LENGTH_SHORT).show()
                            }
                        })
                    },
                    disableCall = {
                        checkAccesses(ctx = ctx, permission = {
                            enable = false
                            scope.launch {
                                ussdApi.callUSSDInvoke(
                                    Constant.disable_forWardCall,
                                    map,
                                    object : USSDController.CallbackInvoke {
                                        override fun responseInvoke(message: String) {
                                        }

                                        override fun over(message: String) {
                                            enable = true
                                            enableDialog = true
                                            textDialog = message
//                                            alertShowDetails(ctx = ctx, modify(message))
                                        }
                                    }, 0
                                )
                            }


                        })
                    },
                    enable
                )

            }

        }


    }


}


fun checkAccesses(ctx: Context, permission: (ctx: Context) -> Unit) {
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


