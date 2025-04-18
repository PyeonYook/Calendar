package com.syu.os.pyeonyukapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.syu.os.pyeonyukapp.ui.AddEventScreen
import com.syu.os.pyeonyukapp.ui.CalendarScreen
import com.syu.os.pyeonyukapp.ui.theme.PyeonyukAppTheme
import com.syu.os.pyeonyukapp.viewmodel.CalendarViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PyeonyukAppTheme {
                val navController = rememberNavController()
                val viewModel: CalendarViewModel = viewModel()

                NavHost(navController = navController, startDestination = "calendar") {
                    composable("calendar") {
                        CalendarScreen(viewModel = viewModel, navController = navController)
                    }
                    composable("addEvent") {
                        AddEventScreen(viewModel = viewModel, navController = navController)
                    }
                }
            }
        }
    }
}
