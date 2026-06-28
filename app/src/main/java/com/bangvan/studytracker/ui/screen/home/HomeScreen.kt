package com.bangvan.studytracker.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bangvan.studytracker.data.local.TaskEntity
import com.bangvan.studytracker.ui.theme.AlertRed
import com.bangvan.studytracker.ui.theme.Background
import com.bangvan.studytracker.ui.theme.LightLavender
import com.bangvan.studytracker.ui.theme.Navy
import com.bangvan.studytracker.ui.theme.Orange
import com.bangvan.studytracker.ui.theme.TagText
import com.bangvan.studytracker.ui.theme.TextLight
import com.bangvan.studytracker.ui.theme.TextPrimary
import com.bangvan.studytracker.ui.theme.TextSecondary
import com.bangvan.studytracker.utils.conditional
import com.bangvan.studytracker.utils.toDeadlineFormat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
   onNavigateToDetail: (Int) -> Unit,
   viewModel: HomeViewModel = hiltViewModel()
) {
   val tasks by viewModel.tasks.collectAsStateWithLifecycle()
   val quoteState by viewModel.quoteState.collectAsStateWithLifecycle()

   Scaffold(
      topBar = {
         CenterAlignedTopAppBar(
            title = {
               Text(text = "StudyTracker",
                  color = Navy,
                  fontWeight = FontWeight.Bold,
                  fontSize = 22.sp)
            },
            navigationIcon = {
               IconButton(onClick = {}) {
                  Icon(imageVector = Icons.Default.AccountCircle,
                     contentDescription = "Profile",
                     tint = TextPrimary,
                     modifier = Modifier.size(28.dp)
                  )
               }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
               containerColor = Background
            )
         )
      },
      bottomBar = {
         BottomNavigationBar()
      },
      floatingActionButton = {
         FloatingActionButton(
            onClick = { onNavigateToDetail(-1) },
            containerColor = Orange,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
         ) {
            Icon(
               imageVector = Icons.Default.Add,
               contentDescription = "Add Task",
               modifier = Modifier.size(28.dp)
            )
         }
      },

      containerColor = Background
   ) {
      innerPadding ->
      Column(
         modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
      ) {
         QuoteSection(quoteState = quoteState, onRefresh = { viewModel.fetchQuote() })

         Spacer(modifier = Modifier.height(20.dp))

         Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
         ){
            Text(
               text = "Upcoming Tasks",
               fontSize = 20.sp,
               fontWeight = FontWeight.Bold,
               color = TextPrimary
            )
            TextButton(onClick = {  }) {
               Text(
                  text = "VIEW ALL",
                  color = Navy,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
               )
            }
         }

         Spacer(modifier = Modifier.height(10.dp))

         if (tasks.isEmpty()) {
            Box(
               modifier = Modifier
                  .fillMaxWidth()
                  .weight(1f),
               contentAlignment = Alignment.Center
            ) {
               Column(
                  horizontalAlignment = Alignment.CenterHorizontally
               ) {
                  Icon(
                     imageVector = Icons.Default.CheckCircle,
                     contentDescription = "No tasks",
                     tint = TextLight,
                     modifier = Modifier.size(64.dp)
                  )
                  Spacer(modifier = Modifier.height(12.dp))
                  Text(
                     text = "Không có nhiệm vụ nào sắp tới!",
                     color = TextSecondary,
                     fontSize = 15.sp,
                     fontWeight = FontWeight.Medium
                  )
                  Text(
                     text = "Bấm nút + để tạo mới nhé.",
                     color = TextLight,
                     fontSize = 13.sp
                  )
               }
            }
         }
         else{
            LazyColumn(
               modifier = Modifier
                  .weight(1f)
                  .fillMaxWidth(),
               verticalArrangement = Arrangement.spacedBy(12.dp),
               contentPadding = PaddingValues(bottom = 16.dp)
            ) {
               items(
                  items = tasks,
                  key = { it.id }
               ) { task ->
                  TaskItemCard(
                     task = task,
                     onToggleCompletion = { viewModel.toggleTaskCompletion(task) },
                     onClick = { onNavigateToDetail(task.id) },
                  ) { viewModel.deleteTask(task) }
               }
            }
         }
      }
   }


}


@Composable
fun TaskItemCard (
   task: TaskEntity,
   onToggleCompletion: () -> Unit,
   onClick: () -> Unit,
   onDelete: () -> Unit,

) {
   Card(
      modifier = Modifier
         .fillMaxWidth()
         .clickable { onClick() }
         .conditional(task.isCompleted) {
            background(Color(0xFFFAFAFA))
         },
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
         containerColor = if (task.isCompleted) Color(0xFFF1F1F1) else Color.White
      ),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      border = if (task.isCompleted) null else androidx.compose.foundation.BorderStroke(
         1.dp,
         Color(0xFFF0F0F0)
      )
   )
   {
      Row(
         modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
         verticalAlignment = Alignment.CenterVertically
      ) {
         Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { onToggleCompletion() },
            colors = CheckboxDefaults.colors(
               checkedColor = Navy,
               uncheckedColor = TextLight
            )
         )
         Spacer(modifier = Modifier.width(12.dp))

         Column(
            modifier = Modifier.weight(1f)
         ) {

            Text(
               text = task.title,
               fontSize = 16.sp,
               fontWeight = FontWeight.Bold,
               color = if (task.isCompleted) TextLight else TextPrimary,
               textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (task.description.isNotEmpty()) {
               Text(
                  text = task.description,
                  fontSize = 13.sp,
                  color = TextSecondary,
                  maxLines = 2
               )
               Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
               verticalAlignment = Alignment.CenterVertically
            ) {
               Icon(
                  imageVector = Icons.Default.DateRange,
                  contentDescription = "Deadline",
                  tint = AlertRed,
                  modifier = Modifier.size(14.dp)
               )
               Spacer(modifier = Modifier.width(4.dp))
               Text(
                  text = task.dueDate.toDeadlineFormat(),
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = AlertRed
               )
            }
         }

         IconButton(
            onClick = onDelete,
            modifier = Modifier.size(36.dp)
         ) {
            Icon(
               imageVector = Icons.Default.Delete,
               contentDescription = "Delete Task",
               tint = TextLight,
               modifier = Modifier.size(20.dp)
            )
         }
      }
   }
}


@Composable
fun QuoteSection(
   quoteState: QuoteUiState,
   onRefresh: ()-> Unit
){
   Card(
      modifier = Modifier.fillMaxWidth()
         .clickable{onRefresh()},
      shape = RoundedCornerShape(20.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      colors = CardDefaults.cardColors(containerColor = LightLavender),
   ) {
      Column(
         modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
         horizontalAlignment = Alignment.CenterHorizontally
      ) {
         Box(
            modifier = Modifier.size(40.dp)
               .clip(CircleShape)
               .background(Color(0xFFD6DBFF)),
            contentAlignment = Alignment.Center
         ){
            Icon(
               imageVector = Icons.Default.Star,
               contentDescription = "Quote Icon",
               tint = TagText,
               modifier = Modifier.size(24.dp)
            )

         }
         Spacer(modifier = Modifier.height(12.dp))

         when (quoteState){
            is QuoteUiState.Loading ->{
               CircularProgressIndicator(
                  color = TagText,
                  modifier = Modifier.size(24.dp)
               )
            }
            is QuoteUiState.Success -> {
               Text(
                  text = "\"${quoteState.quote.quote}\"",
                  fontSize = 17.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextPrimary,
                  textAlign = TextAlign.Center,
                  fontStyle = FontStyle.Normal,
                  lineHeight = 24.sp

               )
               Spacer(modifier = Modifier.height(8.dp))
               Text(
                  text = quoteState.quote.author.uppercase(),
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextSecondary,
                  letterSpacing = 1.sp
               )

            }
            is QuoteUiState.Error -> {
               Text(
                  text = "Không thể tải trích dẫn. Chạm để tải lại.",
                  fontSize = 14.sp,
                  color = TextSecondary,
                  textAlign = TextAlign.Center
               )
            }
         }

      }
   }
}

@Composable
fun BottomNavigationBar(){
   Surface(
      color = Color.White,
      tonalElevation = 8.dp,
      shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
      modifier = Modifier.fillMaxWidth()
   ) {
      Row(
         modifier = Modifier.fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 8.dp),
         horizontalArrangement = Arrangement.SpaceBetween,
         verticalAlignment = Alignment.CenterVertically

      ) {
         val items = listOf(
            Triple("Dashboard", Icons.Default.Home,true),
            Triple("Tasks", Icons.Default.List,false),
            Triple("Timer", Icons.Default.Refresh,false),
            Triple("Profile", Icons.Default.Person,false),
         )

         items.forEach { (label,icon,isSelected) ->
            Box(
               modifier = Modifier.weight(1f)
                  .clip(RoundedCornerShape(16.dp))
                  .clickable{}
                  .padding(vertical = 4.dp),
               contentAlignment = Alignment.Center
            ){
               Column(
                  horizontalAlignment = Alignment.CenterHorizontally,
                  verticalArrangement = Arrangement.Center
               ) {
                  Box(
                     modifier = Modifier.clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) Orange else Color.Transparent)
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                     contentAlignment = Alignment.Center

                  ){
                     Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if(isSelected) Color.White else TextSecondary,
                        modifier = Modifier.size(24.dp)
                     )
                  }

                  Spacer(modifier = Modifier.height(4.dp))

                  Text(
                     text = label,
                     color = if (isSelected) Orange else TextSecondary,
                     fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                     fontSize = 11.sp,
                     maxLines = 1
                  )
               }
            }
         }
      }
   }
}