package com.syu.os.pyeonyukapp.data

import com.google.firebase.firestore.FirebaseFirestore
import com.syu.os.pyeonyukapp.ui.Event
import kotlinx.coroutines.tasks.await

class FirestoreService {

    private val db = FirebaseFirestore.getInstance()
    private val eventCollection = db.collection("events")

    // 🔸 일정 저장
    suspend fun addEvent(event: Event) {
        eventCollection.add(event).await()
    }

    // 🔸 날짜별 일정 가져오기
    suspend fun getEventsByDate(date: String): List<Event> {
        val snapshot = eventCollection
            .whereEqualTo("date", date)
            .get()
            .await()

        return snapshot.toObjects(Event::class.java)
    }

    // 🔸 전체 일정 가져오기 (예시)
    suspend fun getAllEvents(): List<Event> {
        val snapshot = eventCollection.get().await()
        return snapshot.toObjects(Event::class.java)
    }
}
