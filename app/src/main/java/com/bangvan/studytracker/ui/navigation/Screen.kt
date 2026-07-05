package com.bangvan.studytracker.ui.navigation

sealed class Screen (val route: String){
    object Home: Screen("home")
    object TaskDetail : Screen("task_detail/{taskId}"){
        fun passTaskId(taskId: Int): String{
            return "task_detail/$taskId"
        }
    }

    object Splash: Screen("splash")

    object Settings: Screen("settings")

}