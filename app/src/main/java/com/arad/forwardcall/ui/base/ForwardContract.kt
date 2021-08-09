package com.arad.forwardcall.ui.base

class ForwardContract {

    sealed class Event : UiEvent {
        data class ForWardEvent(val message:String) : Event()
        object DisableEvent : Event()
    }

    data class State(
        val state: ForwardState
    ) : UiState
    sealed class ForwardState {
        object Idle : ForwardState()
        data class Forward(val status: String) : ForwardState()
        data class Disable(val status: String) : ForwardState()
    }
    sealed class Effect : UiEffect {
        object Empty : ForwardState()
        data class Dialog(val message:String) : Effect()
    }
}