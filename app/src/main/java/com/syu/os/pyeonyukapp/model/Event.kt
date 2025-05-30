package com.syu.os.pyeonyukapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.syu.os.pyeonyukapp.data.LocalDateTimeConverter
import java.time.LocalDateTime

@IgnoreExtraProperties
data class Event(
    @DocumentId val id: String = "",
    val title: String = "",
    val startDateTimeTimestamp: Timestamp = Timestamp.now(),
    val endDateTimeTimestamp: Timestamp = Timestamp.now(),
    val category: String = "약속",
    val reminder: String = "",
    val repeatType: String = "없음",
    val location: String = "",
    val url: String = "",
    val memo: String = "",
    val color: String = "#2196F3"
) {
    @get:Exclude
    val startDateTime: LocalDateTime
        get() = LocalDateTimeConverter.fromTimestamp(startDateTimeTimestamp)

    @get:Exclude
    val endDateTime: LocalDateTime
        get() = LocalDateTimeConverter.fromTimestamp(endDateTimeTimestamp)

    @Exclude
    fun toMap(): Map<String, Any> {
        return mapOf(
            "title" to title,
            "startDateTimeTimestamp" to LocalDateTimeConverter.toTimestamp(startDateTime),
            "endDateTimeTimestamp" to LocalDateTimeConverter.toTimestamp(endDateTime),
            "category" to category,
            "reminder" to reminder,
            "repeatType" to repeatType,
            "location" to location,
            "url" to url,
            "memo" to memo,
            "color" to color
        )
    }
}
