package com.bangvan.studytracker.ui.screen.detail

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bangvan.studytracker.ui.theme.Background
import com.bangvan.studytracker.ui.theme.BorderColor
import com.bangvan.studytracker.ui.theme.Navy
import com.bangvan.studytracker.ui.theme.Orange
import com.bangvan.studytracker.ui.theme.Surface
import com.bangvan.studytracker.ui.theme.SwitchThumbActive
import com.bangvan.studytracker.ui.theme.SwitchTrackActive
import com.bangvan.studytracker.ui.theme.TextPrimary
import com.bangvan.studytracker.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    taskId: Int,
    onNavigateBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(taskId) {
        viewModel.loadTask(taskId)
    }

    val taskName by viewModel.taskName.collectAsStateWithLifecycle()
    val description by viewModel.description.collectAsStateWithLifecycle()
    val isCompleted by viewModel.isCompleted.collectAsStateWithLifecycle()
    val dueDate by viewModel.dueDate.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val calendar = Calendar.getInstance().apply { timeInMillis = dueDate }

    val dateOnlyFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    val timeOnlyFormat = SimpleDateFormat("hh:mm a", Locale.US)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Task Details",
                        color = Navy,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Background
                )
            )
        },
        containerColor = Background
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Navy)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    border = BorderStroke(1.dp, Color(0xFFEEEEEE))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Task Name",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = taskName,
                            onValueChange = viewModel::onTaskNameChange,
                            placeholder = { "e.g. Midterm Assignment 3" },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Navy,
                                unfocusedBorderColor = BorderColor,
                                focusedLabelColor = Navy
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Detailed Description",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = viewModel::onDescriptionChange,
                            placeholder = { Text("Enter detailed description here...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            maxLines = 5,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Navy,
                                unfocusedBorderColor = BorderColor,
                                focusedLabelColor = Navy
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Due Date",
                                    fontSize = 13.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            DatePickerDialog(
                                                context,
                                                { _, year, month, dayOfMonth ->
                                                    calendar.set(Calendar.YEAR, year)
                                                    calendar.set(Calendar.MONTH, month)
                                                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                                    viewModel.onDueDateChange(calendar.timeInMillis)
                                                },
                                                calendar.get(Calendar.YEAR),
                                                calendar.get(Calendar.MONTH),
                                                calendar.get(Calendar.DAY_OF_MONTH)
                                            ).show()
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Surface),
                                    border = BorderStroke(1.dp, BorderColor)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 14.dp, horizontal = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = "Select Date",
                                            tint = TextSecondary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = dateOnlyFormat.format(Date(dueDate)),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextPrimary
                                        )
                                    }
                                }
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Time",
                                    fontSize = 13.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            TimePickerDialog(
                                                context,
                                                { _, hourOfDay, minute ->
                                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                                    calendar.set(Calendar.MINUTE, minute)
                                                    viewModel.onDueDateChange(calendar.timeInMillis)
                                                },
                                                calendar.get(Calendar.HOUR_OF_DAY),
                                                calendar.get(Calendar.MINUTE),
                                                false
                                            ).show()
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Surface),
                                    border = BorderStroke(1.dp, BorderColor)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 14.dp, horizontal = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Refresh, // Clock icon representation
                                            contentDescription = "Select Time",
                                            tint = TextSecondary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = timeOnlyFormat.format(Date(dueDate)),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextPrimary
                                        )
                                    }
                                }
                            }

                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    border = BorderStroke(1.dp, Color(0xFFEEEEEE))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                    ) {

                        Box(
                            modifier = Modifier
                                .width(6.dp)
                                .fillMaxHeight()
                                .background(Orange)
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(20.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Mark as Completed",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Update your progress for this task",
                                        fontSize = 13.sp,
                                        color = TextSecondary
                                    )
                                }
                                Switch(
                                    checked = isCompleted,
                                    onCheckedChange = viewModel::onCompletedChange,
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = SwitchThumbActive,
                                        checkedTrackColor = SwitchTrackActive,
                                        uncheckedThumbColor = Color.White,
                                        uncheckedTrackColor = BorderColor
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            HorizontalDivider(color = BorderColor, thickness = 1.dp)
                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    viewModel.saveTask {
                                        onNavigateBack()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Navy)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Save",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Save Changes",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }


            }
        }
    }
}
