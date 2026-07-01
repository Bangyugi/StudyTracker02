package com.bangvan.studytracker.ui.screen.splash


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(

) : ViewModel() {

    private val _isSyncing = MutableStateFlow(true)
    val isSyncing: StateFlow<Boolean> = _isSyncing

    init {
        viewModelScope.launch {
            _isSyncing.value = false
        }
    }
}