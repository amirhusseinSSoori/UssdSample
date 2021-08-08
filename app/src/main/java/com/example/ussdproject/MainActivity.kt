package com.example.ussdproject

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ussdproject.ui.forward.ForwardViewModel
import com.example.ussdproject.ui.forward.modify
import com.example.ussdproject.ui.navigation.Nav_graph
import com.example.ussdproject.ui.theme.Purple200

import com.example.ussdproject.ui.theme.UssdProjectTheme
import com.example.ussdproject.util.checkForPermissions
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
                    Nav_graph()
                }
            }
        }
    }


}


















