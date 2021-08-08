package com.example.ussdproject.ui.forward

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arad.ussdlibrary.USSDApi
import com.arad.ussdlibrary.USSDController
import com.example.ussdproject.common.Constant.disable_forWardCall
import com.example.ussdproject.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.HashMap
import java.util.HashSet
import javax.inject.Inject


@HiltViewModel
class ForwardViewModel @Inject constructor(
    var ussdApi: USSDApi,
    var map: HashMap<String, HashSet<String>>
) :   BaseViewModel<ForwardContract.Event, ForwardContract.State, ForwardContract.Effect>() {


//    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<MainState>(MainState.Idle)
    val state: StateFlow<MainState>
        get() = _state

    override fun createInitialState(): ForwardContract.State {
        return ForwardContract.State(
            ForwardContract.ForwardState.Idle
        )
    }

    override fun handleEvent(event: ForwardContract.Event) {
        when (event) {
            is ForwardContract.Event.ForWardEvent -> {
                forward(event.message)
            }
            is ForwardContract.Event.DisableEvent -> {
               disable()
            }
            else -> Unit
        }
    }


    val enabled = MutableLiveData<Boolean>(true)
    val enable:LiveData<Boolean> =enabled


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
                        setState { copy(state = ForwardContract.ForwardState.Forward(status = message)) }
                        setEffect { ForwardContract.Effect.Dialog(message) }
                        enabled.value = true

                    }
                },0)

        }
    }

     fun disable() {
        viewModelScope.launch {
            enabled.value = false
            ussdApi.callUSSDInvoke(
                disable_forWardCall,
                map,
                object : USSDController.CallbackInvoke {
                    override fun responseInvoke(message: String) {
                    }
                    override fun over(message: String) {
//                        setState { copy(state = ForwardContract.ForwardState.Disable(status = message)) }
//                        setEffect { ForwardContract.Effect.Dialog(message) }
                        _state.value=MainState.Disable(message)
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

//
//    sealed class MainIntent {
//        data class  ForWardIntent(var message:String) : MainIntent()
//        object  DisableIntent :MainIntent()
//    }


}


