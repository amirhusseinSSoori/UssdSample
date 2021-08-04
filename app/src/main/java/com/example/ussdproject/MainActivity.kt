package com.example.ussdproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
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













