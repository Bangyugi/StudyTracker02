package com.bangvan.studytracker.ui.navigation

import android.R.attr.type
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.bangvan.studytracker.ui.screen.detail.DetailScreen
import com.bangvan.studytracker.ui.screen.home.HomeScreen
import com.bangvan.studytracker.ui.screen.splash.SplashScreen

@Composable
fun NavGraph (
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(route = Screen.Splash.route){
            SplashScreen(
                onNavigateToHome = {navController.navigate(Screen.Home.route)}
            )
        }

        composable(route = Screen.Home.route){
            HomeScreen(
                onNavigateToDetail = {taskId->
                    navController.navigate(Screen.TaskDetail.passTaskId(taskId))
                }
            )
        }
        composable(
            route = Screen.TaskDetail.route,
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "studytracker://task/{taskId}"
                }
            )
        ){backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: -1
            DetailScreen(
                taskId = taskId,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )

        }
    }
}