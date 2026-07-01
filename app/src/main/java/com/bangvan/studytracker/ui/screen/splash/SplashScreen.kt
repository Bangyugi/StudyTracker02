package com.bangvan.studytracker.ui.screen.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

@Composable
fun SplashScreen (
                  onNavigateToHome:()->Unit,
                  viewModel: SplashViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        val minDelayJob= launch{ delay(1500) }
        val waitDataJob= launch { viewModel.isSyncing.first{!it} }
        joinAll(minDelayJob,waitDataJob)
        onNavigateToHome()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Study Tracker",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))
            CircularProgressIndicator()
        }
    }

}