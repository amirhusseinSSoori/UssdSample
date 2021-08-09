package com.arad.forwardcall.util


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.core.app.ActivityCompat
import com.arad.ussdlibrary.USSDController
import com.arad.forwardcall.R
import com.arad.forwardcall.common.Constant.disableUssd
import com.arad.forwardcall.common.Constant.divert_Active
import com.arad.forwardcall.common.Constant.divert_NotActive
import com.arad.forwardcall.common.Constant.have_problem
import com.arad.forwardcall.common.Constant.invalid
import com.arad.forwardcall.common.Constant.success


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


fun modify(str: String): String {
    var resualt = " "
    val substrings = str.split(" ").toTypedArray()
    for (s in substrings) {
        if (s == success) {
            resualt = divert_Active
        }
        if (s == disableUssd) {
            resualt = divert_NotActive
        }
        if (s == invalid) {
            resualt = have_problem
        }
    }
    return resualt

}


@Composable
fun AlertDialogComponent(openDialog: () -> Unit, message: String) {
    AlertDialog(
        onDismissRequest = openDialog,
        title = {
            Text(
                text = stringResource(R.string.forwardCall),
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                fontFamily = utilFont,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                modify(message),
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                fontFamily = utilFont,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(
                onClick = openDialog
            ) {
                Text(
                    text =
                    stringResource(R.string.cancel), color = Color.White, fontFamily = utilFont,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        },
        backgroundColor = colorResource(id = R.color.purple_700),
        contentColor = Color.White
    )
}


fun checkAccesses(ctx: Context, permission: (ctx: Context) -> Unit) {
    if (checkForPermissions(ctx) && USSDController.verifyAccesibilityAccess(
            ctx
        )
    ) {
        permission(ctx)
    }
}


