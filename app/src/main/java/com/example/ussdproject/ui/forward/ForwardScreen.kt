package com.example.ussdproject.ui.forward

import android.R.attr
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ussdproject.R
import com.example.ussdproject.common.Constant.divert_Active
import com.example.ussdproject.common.Constant.divert_NotActive
import com.example.ussdproject.common.Constant.have_problem
import com.example.ussdproject.ui.theme.*
import com.example.ussdproject.util.utilFont
import java.util.*
import android.R.attr.password
import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import com.example.ussdproject.util.AlertDialogComponent


@Composable
fun Forward(
    text: String,
    onTextChange: (String) -> Unit,
    ussdCall: () -> Unit,
    disableCall: () -> Unit,
    enable: Boolean
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Purple200),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {
                Text(
                    stringResource(R.string.enter_number),
                    fontFamily = utilFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            },
            singleLine = true,
            maxLines = 1,
            textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(
                    20.dp
                )
        )
        CardUssd(
            forwardCall = ussdCall, DisableCall = {
                disableCall()
            }, enabled = enable
        )


    }


}


@Composable
fun CardUssd(
    forwardCall: () -> Unit,
    DisableCall: () -> Unit,
    enabled: Boolean,
) {
    Row(modifier = Modifier.padding(20.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .weight(1f)
                .padding(2.dp)
                .border(2.dp, Purple500)
                .padding(4.dp)
                .border(10.dp, Purple200)
                .padding(
                    start =
                    5.dp, end = 5.dp, top = 5.dp
                ),
            elevation = 10.dp,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable(
                        enabled = enabled,
                        onClick = {
                            DisableCall()
                        }
                    )
                    .background(disableColor)
            ) {
                Text(
                    text = stringResource(R.string.disable), color = black, fontFamily = utilFont,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .weight(1f)
                .padding(2.dp)
                .border(2.dp, Purple500)
                .padding(4.dp)
                .border(10.dp, Purple200)
                .padding(
                    start =
                    10.dp, end = 5.dp, top = 5.dp
                ),
            elevation = 4.dp,
        ) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .clickable(
                        enabled = enabled,
                        onClick = forwardCall
                    )
                    .background(forwardColor)
            ) {
                Text(
                    text = stringResource(R.string.forward), color = black, fontFamily = utilFont,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

            }

        }


    }

}


fun modify(str: String): String {
    var resualt = " "
    val substrings = str.split(" ").toTypedArray()
    for (s in substrings) {
        if (s == "successful.,") {
            resualt = divert_Active
        }
        if (s == "disabled.,") {
            resualt = divert_NotActive
        }
        if (s == "invalid") {
            resualt = have_problem
        }

    }
    return resualt

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







