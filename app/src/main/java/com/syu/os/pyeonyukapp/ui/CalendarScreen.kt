package com.syu.os.pyeonyukapp.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.syu.os.pyeonyukapp.model.Event
import com.syu.os.pyeonyukapp.viewmodel.EventViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.*
import java.util.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items


@OptIn(ExperimentalFoundationApi::class)

@Composable
fun CalendarScreen(
    viewModel: EventViewModel,
    selectedDate: MutableState<LocalDate>,
    onAddEventClick: () -> Unit,
    onEditEventClick: (Event) -> Unit
) {
    val today = remember { LocalDate.now() }
    var currentMonth by remember { mutableStateOf(YearMonth.from(selectedDate.value)) }
    var searchQuery by remember { mutableStateOf("") }
    var filter1 by remember { mutableStateOf("전체") }
    var filter2 by remember { mutableStateOf("전체") }

    val holidays = listOf(
        LocalDate.of(2025, 1, 1), LocalDate.of(2025, 3, 1),
        LocalDate.of(2025, 5, 5), LocalDate.of(2025, 6, 6),
        LocalDate.of(2025, 8, 15), LocalDate.of(2025, 10, 3),
        LocalDate.of(2025, 12, 25)
    )

    val firstDay = currentMonth.atDay(1)
    val firstWeekday = firstDay.dayOfWeek.value % 7
    val daysInMonth = currentMonth.lengthOfMonth()
    val days = List(firstWeekday) { null } + (1..daysInMonth).map {
        LocalDate.of(currentMonth.year, currentMonth.month, it)
    }

    val allEvents by viewModel.events.collectAsStateWithLifecycle()

    val filteredEvents = allEvents.filter { event ->
        event.startDateTime.toLocalDate() == selectedDate.value &&
                (filter1 == "전체" || event.category == filter1 || filter2 == "전체" || event.category == filter2) &&
                event.title.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                placeholder = { Text("일정 검색") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                singleLine = true
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CategoryDropdownButton(filter1, listOf("전체", "학사일정", "시험")) { filter1 = it }
                    CategoryDropdownButton(filter2, listOf("전체", "개인일정", "모임")) { filter2 = it }
                }
                Text("${filteredEvents.size}개", style = MaterialTheme.typography.bodySmall)
            }
        }

        item {
            Text("${currentMonth.monthValue}", fontWeight = FontWeight.Bold, fontSize = 32.sp)
        }

        item {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                listOf("일", "월", "화", "수", "목", "금", "토").forEach {
                    Text(it, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 12.sp)
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .pointerInput(currentMonth) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            if (dragAmount < -20) currentMonth = currentMonth.plusMonths(1)
                            if (dragAmount > 20) currentMonth = currentMonth.minusMonths(1)
                        }
                    }
            ) {
                LazyVerticalGrid(columns = GridCells.Fixed(7)) {
                    items(days) { date ->
                        DayCell(
                            date = date,
                            today = today,
                            selectedDate = selectedDate.value,
                            events = allEvents,
                            holidays = holidays,
                            currentMonth = currentMonth,
                            onClick = { date?.let { selectedDate.value = it } }
                        )
                    }
                }
            }

        }

        item {
            Text(
                "${selectedDate.value.year}년 ${selectedDate.value.monthValue}월 ${selectedDate.value.dayOfMonth}일",
                fontWeight = FontWeight.Bold
            )
        }

        items(filteredEvents) { event ->
            EventCard(event = event, onClick = { onEditEventClick(event) })
        }

        item {
            Button(
                onClick = onAddEventClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("+ 새로운 이벤트", color = Color.White)
            }
        }
    }
}

@Composable
fun DayCell(
    date: LocalDate?,
    today: LocalDate,
    selectedDate: LocalDate,
    events: List<Event>,
    holidays: List<LocalDate>,
    currentMonth: YearMonth,
    onClick: () -> Unit
) {
    val isToday = date == today
    val isSelected = date == selectedDate
    val isHoliday = holidays.contains(date)
    val isFromThisMonth = date?.month == currentMonth.month
    val eventsForDate = events.filter { it.startDateTime.toLocalDate() == date }

    val backgroundColor = when {
        isSelected -> Color.DarkGray
        isToday -> Color(0xFFE0E0E0)
        else -> Color.Transparent
    }

    val textColor = when {
        isSelected -> Color.White
        !isFromThisMonth -> Color.LightGray
        isHoliday || date?.dayOfWeek == DayOfWeek.SUNDAY -> Color.Red
        else -> Color.Black
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(2.dp)
            .clickable(enabled = date != null) { onClick() }
            .background(backgroundColor, RoundedCornerShape(6.dp))
            .padding(4.dp)
    ) {
        Column {
            Text(
                text = date?.dayOfMonth?.toString() ?: "",
                color = textColor,
                fontSize = 12.sp,
                fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal
            )
            eventsForDate.take(2).forEach {
                Text(
                    text = it.title,
                    color = if (isSelected) Color.White else Color.Black,
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (eventsForDate.size > 2) {
                Text(
                    text = "외 ${eventsForDate.size - 2}개",
                    fontSize = 9.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun EventCard(event: Event, onClick: () -> Unit) {
    val startTime = event.startDateTime.toLocalTime().toString()
    val endTime = event.endDateTime.toLocalTime().toString()
    val categoryColor = try {
        Color(android.graphics.Color.parseColor(event.color))
    } catch (e: Exception) {
        Color.Gray
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(text = startTime, fontSize = 12.sp, color = Color.Gray)
                Text(text = endTime, fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = event.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                if (event.memo.isNotEmpty()) {
                    Text(
                        text = event.memo,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryDropdownButton(
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
        ) {
            Text(selected)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = Color.Gray
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
