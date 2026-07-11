package com.bangvan.studytracker.data.repository

import com.bangvan.studytracker.BuildConfig
import com.bangvan.studytracker.data.local.TaskDao
import com.bangvan.studytracker.data.local.TaskEntity
import com.bangvan.studytracker.data.remote.QuoteResponse
import com.bangvan.studytracker.data.remote.QuoteApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private  val taskDao: TaskDao,
    private val quoteApiService: QuoteApiService

){

    fun getAllTasks(): Flow<List<TaskEntity>> = taskDao.getAllTasks()

    suspend fun getTaskById(id: Int): TaskEntity? = taskDao.getTaskById(id)

    suspend fun insertTask(task: TaskEntity): Long = taskDao.insertTask(task)

    suspend fun updateTask(task: TaskEntity) = taskDao.updateTask(task)

    suspend fun deleteTask(task: TaskEntity) = taskDao.deleteTask(task)

    suspend fun getRandomQuote(): List<QuoteResponse> = quoteApiService.getRandomQuote(BuildConfig.QUOTE_API_URL)
}