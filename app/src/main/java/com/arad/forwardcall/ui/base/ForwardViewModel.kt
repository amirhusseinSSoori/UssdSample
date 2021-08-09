package com.arad.forwardcall.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arad.ussdlibrary.USSDApi
import com.arad.ussdlibrary.USSDController
import com.arad.forwardcall.common.Constant.disable_forWardCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.HashMap
import java.util.HashSet
import javax.inject.Inject


@HiltViewModel
class ForwardViewModel @Inject constructor(
    var ussdApi: USSDApi,
    var map: HashMap<String, HashSet<String>>
) : BaseViewModel<ForwardContract.Event, ForwardContract.State, ForwardContract.Effect>()  {





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
                        setState {
                            copy(state = ForwardContract.ForwardState.Disable(message))
                        }
                    }
                },0)

        }
    }



    override fun createInitialState(): ForwardContract.State {
        return ForwardContract.State(state = ForwardContract.ForwardState.Idle)
    }

    override fun handleEvent(event: ForwardContract.Event) {
      when(event){
          is ForwardContract.Event.DisableEvent ->{
              disable()
          }
      }
    }


}


