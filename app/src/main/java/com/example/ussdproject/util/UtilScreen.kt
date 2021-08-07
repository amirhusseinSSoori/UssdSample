package com.example.ussdproject.util


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ussdproject.R


val utilFont = FontFamily(
    Font(R.font.iran_sans_mobile, FontWeight.Normal),
    Font(R.font.iransanse_medium, FontWeight.Medium)
)
public fun checkForPermissions(context: Context): Boolean {
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        ) != PackageManager.PERMISSION_GRANTED

    ) {
        ActivityCompat.requestPermissions(
            context as Activity,
            listOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.ANSWER_PHONE_CALLS
            ).toTypedArray(), 101
        )
        return false
    }

    return true
}



@Composable
fun AlertDialogComponent(openDialog: () -> Unit) {


    AlertDialog(
        // on dialog dismiss we are setting
        // our dialog value to false.
        onDismissRequest = openDialog,

        // below line is use to display title of our dialog
        // box and we are setting text color to white.
        title = { Text(text = "Geeks for Geeks", color = Color.White) },

        // below line is use to display
        // description to our alert dialog.
        text = { Text("Hello! This is our Alert Dialog..", color = Color.White) },

        // in below line we are displaying
        // our confirm button.
        confirmButton = {
            // below line we are adding on click
            // listener for our confirm button.
            TextButton(
                onClick =  openDialog


            ) {
                // in this line we are adding
                // text for our confirm button.
                Text("Confirm", color = Color.White)
            }
        },
        // in below line we are displaying
        // our dismiss button.
        dismissButton = {
            // in below line we are displaying
            // our text button
            TextButton(
                // adding on click listener for this button
                onClick = openDialog

            ) {
                // adding text to our button.
                Text("Dismiss", color = Color.White)
            }
        },
        // below line is use to add background color to our alert dialog
        backgroundColor = colorResource(id = R.color.teal_200),

        // below line is use to add content color for our alert dialog.
        contentColor = Color.White
    )
}

