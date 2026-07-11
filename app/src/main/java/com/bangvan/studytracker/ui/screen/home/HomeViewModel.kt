package com.bangvan.studytracker.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangvan.studytracker.data.local.TaskEntity
import com.bangvan.studytracker.data.remote.QuoteResponse
import com.bangvan.studytracker.data.repository.RemoteConfigRepository
import com.bangvan.studytracker.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject



sealed interface QuoteUiState {
    object Loading : QuoteUiState
    data class Success(val quote: QuoteResponse) : QuoteUiState
    data class Error(val message: String) : QuoteUiState
}
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val remoteConfigRepository: RemoteConfigRepository
): ViewModel() {
    private val tag = "StudyTrackerLog"

    private val _quoteState = MutableStateFlow<QuoteUiState>(QuoteUiState.Loading)

    val quoteState: StateFlow<QuoteUiState> = _quoteState.asStateFlow()

    private val _showBanner = MutableStateFlow(false)
    val showBanner: StateFlow<Boolean> = _showBanner.asStateFlow()

    private val _welcomeMessage = MutableStateFlow("")
    val welcomeMessage: StateFlow<String> = _welcomeMessage.asStateFlow()

    val tasks: StateFlow<List<TaskEntity>> = repository.getAllTasks()
        .onEach { list -> Log.d(tag,"Task updated: Loaded ${list.size} tasks from local database") }
        .stateIn(scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList())

    init {
        Log.d(tag, "HomeViewModel initialized.")
        fetchQuote()

        loadCachedValues()

        fetchRemoteConfig()

    }

    private fun loadCachedValues(){
        _showBanner.value =  remoteConfigRepository.getBoolean("show_banner")
        _welcomeMessage.value = remoteConfigRepository.getString("welcome_message")
    }

    private fun fetchRemoteConfig(){
        remoteConfigRepository.fetchAndActivate { isSuccessful ->
            if (isSuccessful){
                loadCachedValues()
            }
        }
    }

    fun fetchQuote(){
       viewModelScope.launch {
           _quoteState.value = QuoteUiState.Loading
           try {
               val quote = repository.getRandomQuote()
               _quoteState.value = QuoteUiState.Success(quote[0] )
           } catch (e: Exception)
           {
               android.util.Log.e("StudyTrackerLog", "Lỗi tải quote", e)
               _quoteState.value = QuoteUiState.Error(e.message ?: "Unknown")
           }

       }
    }

    fun toggleTaskCompletion(task: TaskEntity){
        viewModelScope.launch {
            val newStatus = !task.isCompleted
            repository.updateTask(task.copy(isCompleted = newStatus))
        }
    }

    fun deleteTask(task: TaskEntity){
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }


}