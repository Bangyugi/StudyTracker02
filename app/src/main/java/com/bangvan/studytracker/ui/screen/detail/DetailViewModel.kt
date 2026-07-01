package com.bangvan.studytracker.ui.screen.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangvan.studytracker.data.local.TaskEntity
import com.bangvan.studytracker.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: TaskRepository,

) : ViewModel() {

    private val tag = "StudyTrackerLog"

    private val _taskName = MutableStateFlow("")
    val taskName = _taskName.asStateFlow()

    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted = _isCompleted.asStateFlow()

    private val _dueDate = MutableStateFlow(System.currentTimeMillis())
    val dueDate = _dueDate.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var currentTaskId: Int = -1

    fun loadTask(taskId: Int) {
        if (taskId == -1) {
            return
        }
        if (taskId == currentTaskId) return
        currentTaskId = taskId

        viewModelScope.launch {
            _isLoading.value = true
            Log.d(tag, "loadTask: Fetching task details from DB for ID: $taskId")
            val task = repository.getTaskById(taskId)
            if (task != null) {
                _taskName.value = task.title
                _description.value = task.description
                _isCompleted.value = task.isCompleted
                _dueDate.value = task.dueDate
                Log.d(tag, "loadTask: Task details loaded successfully for '${task.title}'")
            } else {
                Log.w(tag, "loadTask: Task with ID $taskId was not found in the database.")
            }
            _isLoading.value = false
        }


    }

    fun onTaskNameChange(value: String) {
        _taskName.value = value
    }

    fun onDescriptionChange(value: String) {
        _description.value = value
    }

    fun onCompletedChange(value: Boolean) {
        _isCompleted.value = value
    }

    fun onDueDateChange(value: Long) {
        _dueDate.value = value
    }


    fun saveTask(onSuccess: () -> Unit) {
        val name = _taskName.value.trim()
        if (name.isEmpty()) {
            Log.w(tag, "saveTask: Task name is empty. Cannot save task.")
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            val task = TaskEntity(
                id = if (currentTaskId == -1) 0 else currentTaskId,
                title = name,
                description = _description.value.trim(),
                dueDate = _dueDate.value,
                isCompleted = _isCompleted.value
            )
            Log.d(
                tag,
                "saveTask: Saving task to database (ID: ${if (currentTaskId == -1) "New" else currentTaskId}, Name: '$name')"
            )
            if (currentTaskId == -1) {
                val newId = repository.insertTask(task)
                Log.d(tag, "saveTask: Successfully inserted new task. Generated ID: $newId")
            } else {
                repository.updateTask(task)
                Log.d(tag, "saveTask: Successfully updated task (ID: $currentTaskId)")
            }
            _isLoading.value = false
            onSuccess()
        }
    }

    fun deleteTask(onSuccess:() -> Unit){
        if(currentTaskId == -1) return
        viewModelScope.launch {
            _isLoading.value = true
            val task = repository.getTaskById(currentTaskId)
            if (task != null)
            {
                repository.deleteTask(task)
                Log.d(tag, "deleteTask: Successfully deleted task (ID: $currentTaskId)")
                onSuccess()
            }
            else{
                Log.w(tag, "deleteTask: Task with ID $currentTaskId was not found in the database.")
            }
            _isLoading.value = false
        }
    }

}