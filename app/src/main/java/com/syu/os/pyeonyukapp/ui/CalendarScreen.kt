package com.syu.os.pyeonyukapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syu.os.pyeonyukapp.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onAddEventClick: () -> Unit // NavController 없는 구조를 위한 콜백
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val today = LocalDate.now()
    val days = remember(currentMonth) { getDaysOfMonth(currentMonth) }

    Column(modifier = Modifier.fillMaxSize()) {
        // ⬆️ 월 표기 및 이동
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("◀", modifier = Modifier.clickable { currentMonth = currentMonth.minusMonths(1) }, fontSize = 20.sp)
            Text("${currentMonth.monthValue}월", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("▶", modifier = Modifier.clickable { currentMonth = currentMonth.plusMonths(1) }, fontSize = 20.sp)
        }

        // 📆 달력 그리드
        CalendarGrid(
            days = days,
            today = today,
            selectedDate = selectedDate,
            onDateClick = { selectedDate = it }
        )

        // 📌 선택한 날짜 일정 표시
        TodoListSection(selectedDate = selectedDate, viewModel = viewModel)

        // ➕ 새로운 이벤트 버튼 (일정 색상과 동일)
        Button(
            onClick = { onAddEventClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = getEventColor(viewModel, selectedDate)
            )
        ) {
            Text(text = "+ 새로운 이벤트", color = Color.White)
        }
    }
}

@Composable
fun CalendarGrid(days: List<LocalDate?>, today: LocalDate?, selectedDate: LocalDate?, onDateClick: () -> Unit) {

}

// 날짜 리스트 생성
fun getDaysOfMonth(month: YearMonth): List<LocalDate?> {
    val firstDay = month.atDay(1)
    val lastDay = month.atEndOfMonth()
    val startIndex = firstDay.dayOfWeek.value % 7  // 일요일을 0으로
    val totalDays = lastDay.dayOfMonth

    val days = mutableListOf<LocalDate?>()
    repeat(startIndex) { days.add(null) }
    repeat(totalDays) { i -> days.add(month.atDay(i + 1)) }
    return days
}

// 일정 색상 반환
fun getEventColor(viewModel: CalendarViewModel, date: LocalDate): Color {
    val events = viewModel.getEventsForDate(date)
    return events.firstOrNull()?.color ?: MaterialTheme.colorScheme.primary
}
