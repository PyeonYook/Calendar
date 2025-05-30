package com.syu.os.pyeonyukapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.syu.os.pyeonyukapp.ui.NavGraph
import com.syu.os.pyeonyukapp.util.AlarmHelper
import com.syu.os.pyeonyukapp.viewmodel.EventViewModel
import com.syu.os.pyeonyukapp.viewmodel.EventViewModelFactory
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.activity.viewModels


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel(this)
        requestNotificationPermissionIfNeeded()


        setContent {
            val eventViewModel: EventViewModel by viewModels {
                EventViewModelFactory(applicationContext)
            }

            NavGraph(eventViewModel = eventViewModel)
        }
    }

    // Android 13 이상 알림 권한 요청
    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // 이미 허용됨
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    AlertDialog.Builder(this)
                        .setTitle("알림 권한 필요")
                        .setMessage("일정 알림을 위해 알림 권한이 필요합니다.")
                        .setPositiveButton("허용") { _, _ ->
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                        .setNegativeButton("취소", null)
                        .show()
                }
                else -> {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            // 권한 거부됨
        }
    }
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "event_channel",
                "일정 알림",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "일정 시작 전에 알려주는 알림 채널"
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}
