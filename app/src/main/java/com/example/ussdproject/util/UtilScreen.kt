package com.example.ussdproject.util


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ussdproject.R
import com.example.ussdproject.common.Constant


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

fun alertShowDetails(
    ctx: Context,
    Description: String,
): AlertDialog {
    val dialogBuilder = AlertDialog.Builder(ctx).create()
    dialogBuilder.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
    dialogBuilder.setCancelable(false)
    val dialogView =
        LayoutInflater.from(ctx).inflate(R.layout.dialog_show_notify, null)
    dialogBuilder.window!!.attributes.windowAnimations = R.style.DialogAnimation
    val close = dialogView.findViewById<TextView>(R.id.btn_dialog_retun)
    val msg = dialogView.findViewById<TextView>(R.id.txt_dialog_txt_notify)
    msg.text = Description
    close.setOnClickListener {
        dialogBuilder.dismiss()

    }
    dialogBuilder.setView(dialogView)
    dialogBuilder.show()
    return dialogBuilder
}

fun modify(str: String): String {
    var resualt = " "
    val substrings = str.split(" ").toTypedArray()
    for (s in substrings) {
        if (s == "successful.,") {
            resualt = Constant.divert_Active
        }
        if (s == "disabled.,") {
            resualt = Constant.divert_NotActive
        }
        if (s == "invalid") {
            resualt = Constant.have_problem
        }

    }
    return resualt

}


@Composable
fun AlertDialogComponent(openDialog: () -> Unit,message:String) {


    AlertDialog(
        onDismissRequest = openDialog,
        title = { Text(text = "Forward Call",modifier = Modifier.fillMaxWidth(), color = Color.White,fontFamily = utilFont,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center) },


        text = { Text(modify(message),modifier = Modifier.fillMaxWidth(), color = Color.White,fontFamily = utilFont,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center) },
        confirmButton = {
            TextButton(
                onClick =  openDialog


            ) {
                Text("بازگشت", color = Color.White,fontFamily = utilFont,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center)
            }
        },
        backgroundColor = colorResource(id = R.color.purple_700),
        contentColor = Color.White
    )
}

