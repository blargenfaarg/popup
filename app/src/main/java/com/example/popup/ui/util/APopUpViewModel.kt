package com.example.popup.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Basic template for a view model. Defines the ui event channel and handles sending
 * events to it, as well as defines a method to receive view model events
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/22/2024
 */
abstract class APopUpViewModel<T: ViewModelEvent>: ViewModel() {

    /**
     * The ui event channel used to propagate ui events to the view this
     * model is responsible for
     */
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    /**
     * Method to be implemented by child classes -> should handle all the possible
     * events for the view
     */
    abstract fun onEvent(event: T)

    /**
     * Send a UiEvent to the channel - calls it in a coroutine context of this view model
     */
    protected fun sendUiEventToChannel(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(element = event)
        }
    }
}