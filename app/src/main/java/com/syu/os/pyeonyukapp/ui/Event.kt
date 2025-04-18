package com.syu.os.pyeonyukapp.model

import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime

data class Event(
    val title: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val color: Color = Color(0xFF3F51B5) // 기본 색상
)
