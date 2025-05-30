package com.syu.os.pyeonyukapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syu.os.pyeonyukapp.data.FirestoreService
import com.syu.os.pyeonyukapp.model.Event
import com.syu.os.pyeonyukapp.util.AlarmHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventViewModel(private val context: Context) : ViewModel() {

    private val firestoreService = FirestoreService()
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    init {
        observeEvents()
    }

    private fun observeEvents() {
        firestoreService.observeEvents { newEvents ->
            _events.value = newEvents
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            firestoreService.addEvent(event)
            AlarmHelper.scheduleAlarm(context, event)  // 알림 등록
        }
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            firestoreService.updateEvent(event)
            AlarmHelper.scheduleAlarm(context, event)  // 알림 재등록
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            firestoreService.deleteEvent(event.id)
            AlarmHelper.cancelAlarm(context, event)  // 알림 취소
        }
    }
}
