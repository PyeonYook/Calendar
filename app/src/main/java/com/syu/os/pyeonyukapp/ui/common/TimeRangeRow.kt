package com.syu.os.pyeonyukapp.ui.common

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun TimeRangeRow(
    start: LocalDateTime,
    end: LocalDateTime,
    onStartChange: (LocalDateTime) -> Unit,
    onEndChange: (LocalDateTime) -> Unit
) {
    val context = LocalContext.current
    val dateFormatter = DateTimeFormatter.ofPattern("M월 d일 (E)", Locale.KOREAN)
    val timeFormatter = DateTimeFormatter.ofPattern("a h:mm", Locale.KOREAN)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(imageVector = Icons.Default.AccessTime, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)

        Column(modifier = Modifier.clickable {
            showTimePicker(context, start.toLocalTime()) { newTime ->
                onStartChange(start.withHour(newTime.hour).withMinute(newTime.minute))
            }
        }) {
            Text(start.format(dateFormatter), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(start.format(timeFormatter), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Text(">", modifier = Modifier.padding(horizontal = 4.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)

        Column(modifier = Modifier.clickable {
            showTimePicker(context, end.toLocalTime()) { newTime ->
                onEndChange(end.withHour(newTime.hour).withMinute(newTime.minute))
            }
        }) {
            Text(end.format(dateFormatter), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(end.format(timeFormatter), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.weight(1f))

        Text("하루 종일", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
