package com.example.ussdproject.ui.forward

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arad.ussdlibrary.USSDApi
import com.arad.ussdlibrary.USSDController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.util.HashMap
import java.util.HashSet
import javax.inject.Inject


@HiltViewModel
class ForwardViewModel @Inject constructor(
    var ussdApi: USSDApi,
    var map: HashMap<String, HashSet<String>>
) : ViewModel() {


    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<MainState>(MainState.Idle)
    val state: StateFlow<MainState>
        get() = _state



    val enabled = MutableLiveData<Boolean>(true)
    val enable:LiveData<Boolean> =enabled


    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.ForWardIntent -> forward(it.message)
                    is MainIntent.DisableIntent -> disable()
                }
            }}



        }


    private fun forward(phoneNumber:String) {
        viewModelScope.launch {
            enabled.value = false
            ussdApi.callUSSDInvoke(
                "*21*$phoneNumber#",
                map,
                object : USSDController.CallbackInvoke {
                    override fun responseInvoke(message: String) {
                    }
                    override fun over(message: String) {
                       _state.value= MainState.ForWard(message)
                        enabled.value = true

                    }
                },0)

        }
    }

    private fun disable() {
        viewModelScope.launch {

            enabled.value = false
            ussdApi.callUSSDInvoke(
                "#21#",
                map,
                object : USSDController.CallbackInvoke {
                    override fun responseInvoke(message: String) {
                    }
                    override fun over(message: String) {
                         _state.value= MainState.Disable(message)
                          enabled.value = true

                    }
                },0)

        }
    }

    sealed class MainState {
        object Idle : MainState()
        data class ForWard(var call: String) : MainState()
        data class Disable(var disable: String) : MainState()
    }


    sealed class MainIntent {
        data class  ForWardIntent(var message:String) : MainIntent()
        object  DisableIntent  : MainIntent()
    }
}


