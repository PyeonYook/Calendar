package com.syu.os.pyeonyukapp.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.syu.os.pyeonyukapp.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(navController: NavController, viewModel: CalendarViewModel) {
    val context = LocalContext.current

    var title by remember { mutableStateOf(TextFieldValue("")) }
    var memo by remember { mutableStateOf(TextFieldValue("")) }
    var location by remember { mutableStateOf(TextFieldValue("")) }
    var url by remember { mutableStateOf(TextFieldValue("")) }
    var category by remember { mutableStateOf("과제") }

    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }
    var startTime by remember { mutableStateOf(LocalTime.of(10, 0)) }
    var endTime by remember { mutableStateOf(LocalTime.of(11, 0)) }

    var reminder by remember { mutableStateOf("30분 전") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(20.dp)) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("제목") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "시작: $startDate ${startTime.hour}:${startTime.minute.toString().padStart(2, '0')}",
                    modifier = Modifier.clickable {
                        DatePickerDialog(context, { _, y, m, d -> startDate = LocalDate.of(y, m + 1, d) },
                            startDate.year, startDate.monthValue - 1, startDate.dayOfMonth).show()
                        TimePickerDialog(context, { _, h, min -> startTime = LocalTime.of(h, min) },
                            startTime.hour, startTime.minute, false).show()
                    }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "종료: $endDate ${endTime.hour}:${endTime.minute.toString().padStart(2, '0')}",
                    modifier = Modifier.clickable {
                        DatePickerDialog(context, { _, y, m, d -> endDate = LocalDate.of(y, m + 1, d) },
                            endDate.year, endDate.monthValue - 1, endDate.dayOfMonth).show()
                        TimePickerDialog(context, { _, h, min -> endTime = LocalTime.of(h, min) },
                            endTime.hour, endTime.minute, false).show()
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "일정 종류")
            Row {
                listOf("과제", "시험", "개인일정").forEach { option ->
                    FilterChip(
                        selected = category == option,
                        onClick = { category = option },
                        label = { Text(option) },
                        modifier = Modifier.padding(end = 8.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            labelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = reminder,
                onValueChange = { reminder = it },
                label = { Text("알림") },
                leadingIcon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("위치") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("URL") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = memo,
                onValueChange = { memo = it },
                label = { Text("메모") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(80.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = {
                    val event = Event(
                        title = title.text,
                        startDateTime = LocalDateTime.of(startDate, startTime),
                        endDateTime = LocalDateTime.of(endDate, endTime),
                        category = category,
                        reminder = reminder,
                        location = location.text,
                        url = url.text,
                        memo = memo.text ,
                        color = Color(0xFF81D4FA)
                    )
                    viewModel.addEvent(event)
                    navController.popBackStack()
                },
                containerColor = Color.Black
            ) {
                Text("✔", color = Color.White, fontSize = 20.sp)
            }
        }
    }
}
