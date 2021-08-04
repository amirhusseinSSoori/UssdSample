package com.example.ussdproject.ui.forward

import android.content.Context
import android.graphics.Paint
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arad.ussdlibrary.USSDController
import com.example.ussdproject.R
import com.example.ussdproject.ui.theme.*
import com.example.ussdproject.util.checkForPermissions
import com.example.ussdproject.util.utilFont


@Composable
fun Forward(
    viewModel: ForwardViewModel,
    text: String,
    onTextChange: (String) -> Unit,
    ussdCall: () -> Unit,
    disableCall: () -> Unit,
    enable:Boolean
) {







    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize().background(color= Purple200),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {
                Text(
                    "شماره مورد نظر راوارد نمایید",
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
                .fillMaxWidth().height(130.dp)
                .padding(
                    20.dp
                )
        )
        CardUssd(forwardCall = ussdCall, DisableCall = disableCall, context = context,text = text,
            enabled = enable
        )
        observeViewModel(viewModel)

    }


}


@Composable
fun CardUssd(
    text:String,
    forwardCall: () -> Unit,
    DisableCall: () -> Unit, context: Context,
    enabled:Boolean
) {
    Row(modifier = Modifier.padding(20.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .weight(1f)
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
                            if (checkForPermissions(context) && USSDController.verifyAccesibilityAccess(
                                    context
                                )){
                                DisableCall()
                            }
                        }
                    )
                    .background(disableColor)
            ) {
                Text(
                    text = "Disable", color = black, fontFamily = utilFont,
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
                .padding(
                    start =
                    10.dp, end = 5.dp, top = 5.dp
                ),
            elevation = 4.dp,
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .clickable(
                    enabled = enabled,
                    onClick = {
                        if (checkForPermissions(context) && USSDController.verifyAccesibilityAccess(
                                context
                            )){
                            if(text.trim()!=""){
                                forwardCall()
                            }else{
                                Toast.makeText(context,"لطفا شماره مورد نظر را وارد نمایید",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
                .background(forwardColor)) {
                Text(
                    text = "Forward", color = black, fontFamily = utilFont,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

            }
        }


    }

}


@Composable
fun DescriptionCall(message: String) {
    Text(
        text = message, color = black, fontFamily = utilFont,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

}


@Composable
private fun observeViewModel(forwardViewModel: ForwardViewModel) {
    val data by forwardViewModel.state.collectAsState()
    data.let {
        when (it) {
            is ForwardViewModel.MainState.Idle -> Unit
            is ForwardViewModel.MainState.ForWard -> {
                DescriptionCall(modify(it.call))
            }
            is ForwardViewModel.MainState.Disable -> {
                DescriptionCall(modify(it.disable))
            }


        }

    }

}



fun modify(str:String):String{

    var a=" "
    val substrings = str.split(" ").toTypedArray()

    for (s in substrings) {
        if(s=="successful.,"){
            a="شماره مورد نظر دایورت شد"
        }
        if(s=="disabled.,"){
            a="شماره مورد نظر غیر فعال شد"
        }
        if(s=="invalid"){
            a="مشکلی پیش آمده است لطفا دوباره تلاش کنید"
        }

    }

    return a

}
fun enabled(b:Boolean):Boolean{
    return b
}









