package com.arad.forwardcall.ui.forward


import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arad.forwardcall.R
import com.arad.ussdlibrary.USSDApi

import com.arad.forwardcall.common.Constant
import com.arad.forwardcall.ui.theme.*
import com.arad.forwardcall.util.AlertDialogComponent
import com.arad.forwardcall.util.checkAccesses
import com.arad.forwardcall.util.utilFont
import com.arad.ussdlibrary.USSDController
import java.util.HashMap
import java.util.HashSet

//    onTextChange: (String) -> Unit,
@Composable
fun Forward(
    map: HashMap<String, HashSet<String>>,
    ussdApi: USSDApi
) {
    val (text, SetText) = remember { mutableStateOf("") }
    ForwardScreen(inputNumber = text, onTextChange = SetText, map = map, ussdApi = ussdApi)
}

@Composable
fun ForwardScreen(
    inputNumber: String,
    onTextChange: (String) -> Unit,
    map: HashMap<String, HashSet<String>>,
    ussdApi: USSDApi
) {
    val ctx = LocalContext.current
    var enableButton by remember { mutableStateOf(true) }
    var enableDialog by remember { mutableStateOf(false) }
    var textDialog by remember { mutableStateOf("") }


    if (enableDialog) {
        AlertDialogComponent(openDialog = {
            enableDialog = false
        }, textDialog)

    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Purple200),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = inputNumber,
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
        CardUssd(enabled = enableButton,
            forwardCall = {
                checkAccesses(ctx = ctx, permission = {
                    if (inputNumber.trim() != "") {
                        enableButton = false
                        ussdApi.callUSSDInvoke(
                            "*21*$inputNumber#",
                            map,
                            object : USSDController.CallbackInvoke {
                                override fun responseInvoke(message: String) {
                                }

                                override fun over(message: String) {
                                    enableDialog = true
                                    textDialog = message
                                    enableButton = true

                                }
                            }, 0
                        )
                    } else {
                        Toast.makeText(it, Constant.input_number, Toast.LENGTH_SHORT).show()
                    }
                })
            }, DisableCall = {
                checkAccesses(ctx = ctx, permission = {
                    enableButton = false
                    ussdApi.callUSSDInvoke(
                        Constant.disable_forWardCall,
                        map,
                        object : USSDController.CallbackInvoke {
                            override fun responseInvoke(message: String) {
                            }

                            override fun over(message: String) {
                                enableButton = true
                                enableDialog = true
                                textDialog = message
                            }
                        }, 0
                    )
                })
            }
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
                        onClick = DisableCall)
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









