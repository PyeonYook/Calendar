package com.syu.os.pyeonyukapp.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.syu.os.pyeonyukapp.model.Event
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getAllEvents(): List<Event> {
        return try {
            val snapshot = firestore.collection("events").get().await()
            snapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(Event::class.java)?.copy(id = doc.id)
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun observeEvents(onEventsChanged: (List<Event>) -> Unit) {
        firestore.collection("events")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    onEventsChanged(emptyList())
                    return@addSnapshotListener
                }

                val events = snapshot.documents.mapNotNull { doc ->
                    try {
                        doc.toObject(Event::class.java)?.copy(id = doc.id)
                    } catch (e: Exception) {
                        null
                    }
                }
                onEventsChanged(events)
            }
    }

    suspend fun addEvent(event: Event) {
        try {
            firestore.collection("events")
                .add(event.toMap()).await()
        } catch (_: Exception) {
        }
    }

    suspend fun updateEvent(event: Event) {
        try {
            firestore.collection("events")
                .document(event.id)
                .set(event.toMap()).await()
        } catch (_: Exception) {
        }
    }

    suspend fun deleteEvent(eventId: String) {
        try {
            firestore.collection("events")
                .document(eventId)
                .delete().await()
        } catch (_: Exception) {
        }
    }
}
