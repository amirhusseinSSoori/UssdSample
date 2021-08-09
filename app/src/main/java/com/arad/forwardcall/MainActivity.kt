package com.arad.forwardcall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import com.arad.ussdlibrary.USSDApi


import com.arad.forwardcall.ui.navigation.Nav_graph
import com.arad.forwardcall.ui.theme.Purple200

import com.arad.forwardcall.ui.theme.UssdProjectTheme
import com.arad.forwardcall.util.checkForPermissions
import dagger.hilt.android.AndroidEntryPoint
import java.util.HashMap
import java.util.HashSet
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var ussdApi: USSDApi
    @Inject
    lateinit var map: HashMap<String, HashSet<String>>
    override fun onStart() {
        super.onStart()
        checkForPermissions(this)
    }

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            Surface(color = Purple200) {
                UssdProjectTheme {
                    Nav_graph(map,ussdApi)
                }
            }
        }
    }


}


















