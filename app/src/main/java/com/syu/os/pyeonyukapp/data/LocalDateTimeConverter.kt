package com.syu.os.pyeonyukapp.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

object LocalDateTimeConverter {

    fun toTimestamp(localDateTime: LocalDateTime): Timestamp {
        val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
        return Timestamp(Date.from(instant))
    }

    fun fromTimestamp(timestamp: Timestamp?): LocalDateTime {
        return timestamp?.toDate()?.toInstant()
            ?.atZone(ZoneId.systemDefault())
            ?.toLocalDateTime() ?: LocalDateTime.now()
    }
}
