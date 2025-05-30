package com.syu.os.pyeonyukapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syu.os.pyeonyukapp.data.LocalDateTimeConverter
import com.syu.os.pyeonyukapp.model.Event
import com.syu.os.pyeonyukapp.viewmodel.EventViewModel
import com.syu.os.pyeonyukapp.ui.common.InputRow
import com.syu.os.pyeonyukapp.ui.common.TimeRangeRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventScreen(
    event: Event,
    viewModel: EventViewModel,
    onSave: () -> Unit
) {
    var title by remember { mutableStateOf(event.title) }
    var startDateTime by remember { mutableStateOf(event.startDateTime) }
    var endDateTime by remember { mutableStateOf(event.endDateTime) }
    var category by remember { mutableStateOf(event.category) }
    var reminder by remember { mutableStateOf(event.reminder) }
    var repeatType by remember { mutableStateOf(event.repeatType) }
    var location by remember { mutableStateOf(event.location) }
    var url by remember { mutableStateOf(event.url) }
    var memo by remember { mutableStateOf(event.memo) }

    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "메뉴")
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("삭제") },
                            onClick = {
                                viewModel.deleteEvent(event)
                                onSave()
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val updated = event.copy(
                        title = title,
                        startDateTimeTimestamp = LocalDateTimeConverter.toTimestamp(startDateTime),
                        endDateTimeTimestamp = LocalDateTimeConverter.toTimestamp(endDateTime),
                        category = category,
                        reminder = reminder,
                        repeatType = repeatType,
                        location = location,
                        url = url,
                        memo = memo
                    )
                    viewModel.updateEvent(updated)
                    onSave()
                },
                containerColor = Color.Black
            ) {
                Icon(Icons.Default.Check, contentDescription = "저장", tint = Color.White)
            }
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("일정 제목 입력", fontWeight = FontWeight.Bold) },
                textStyle = LocalTextStyle.current.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )

            TimeRangeRow(
                start = startDateTime,
                end = endDateTime,
                onStartChange = { startDateTime = it },
                onEndChange = { endDateTime = it }
            )

            Text("반복", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("없음", "매일", "매주", "매월").forEach { type ->
                    FilterChip(
                        selected = repeatType == type,
                        onClick = { repeatType = type },
                        label = { Text(type) }
                    )
                }
            }

            Text("종류", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("약속", "학사일정", "개인일정", "모임").forEach { type ->
                    FilterChip(
                        selected = category == type,
                        onClick = { category = type },
                        label = { Text(type) }
                    )
                }
            }

            Text("알림", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                listOf("없음", "10분 전", "30분 전", "1시간 전", "1일 전", "1주 전").forEach { time ->
                    FilterChip(
                        selected = reminder == time,
                        onClick = { reminder = time },
                        label = { Text(time) }
                    )
                }
            }

            InputRow(Icons.Default.Place, location, { location = it }, "위치 입력")
            InputRow(Icons.Default.Link, url, { url = it }, "URL 입력")
            InputRow(Icons.Default.EditNote, memo, { memo = it }, "메모 작성")
        }
    }
}
