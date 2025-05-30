package com.syu.os.pyeonyukapp.ui

import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*
import com.syu.os.pyeonyukapp.model.Event
import com.syu.os.pyeonyukapp.viewmodel.EventViewModel
import java.time.LocalDate

@Composable
fun NavGraph(
    eventViewModel: EventViewModel
) {
    val navController = rememberNavController()
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val selectedEvent = remember { mutableStateOf<Event?>(null) }

    NavHost(navController = navController, startDestination = "calendar") {
        composable("calendar") {
            CalendarScreen(
                viewModel = eventViewModel,
                selectedDate = selectedDate,
                onAddEventClick = { navController.navigate("add_event") },
                onEditEventClick = {
                    selectedEvent.value = it
                    navController.navigate("edit_event")
                }
            )
        }

        composable("add_event") {
            AddEventScreen(
                selectedDate = selectedDate.value,
                viewModel = eventViewModel,
                onSave = { navController.popBackStack() }
            )
        }

        composable("edit_event") {
            selectedEvent.value?.let { event ->
                EditEventScreen(
                    event = event,
                    viewModel = eventViewModel,
                    onSave = { navController.popBackStack() }
                )
            }
        }
    }
}
