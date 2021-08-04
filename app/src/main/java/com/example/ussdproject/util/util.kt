package com.example.ussdproject.util

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.ussdproject.MainActivity

@Preview("project")
@Composable
fun A(){
CustomMobile(text = "fdgdg,")
}




@Composable
fun CustomMobile(text:String,modifier: Modifier = Modifier) {


    Column(modifier = modifier) {
        Text(text = "fgdfgdsfgdg")
    }


}