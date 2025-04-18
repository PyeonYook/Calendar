package com.syu.os.pyeonyukapp.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.syu.os.pyeonyukapp.ui.Event
import java.time.LocalDate
import java.time.LocalDateTime

class CalendarViewModel : ViewModel() {
    private val events = listOf(
        Event("중간고사", LocalDateTime.of(2025, 4, 22, 10, 0), LocalDateTime.of(2025, 4, 28, 10, 0), Color(0xFFBA68C8)),
        Event("멀미 10시 강의실에서 시험", LocalDateTime.of(2025, 4, 28, 10, 0), LocalDateTime.of(2025, 4, 28, 11, 0), Color(0xFF64B5F6)),
        Event("데베 오프라인 정상수업", LocalDateTime.of(2025, 4, 28, 11, 0), LocalDateTime.of(2025, 4, 29, 12, 0), Color(0xFF64B5F6))
    )

    fun getEventsForDate(date: LocalDate): List<Event> {
        return events.filter { it.startDateTime.toLocalDate() <= date && it.endDateTime.toLocalDate() >= date }
    }
}
