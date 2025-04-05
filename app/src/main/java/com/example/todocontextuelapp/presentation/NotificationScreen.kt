package com.example.todocontextuelapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NotificationScreen(notificationViewModel: NotificationViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { notificationViewModel.showSimpleNotification() }) {
            Text(text = "Show Notification")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { notificationViewModel.updateNotification() }) {
            Text(text = "Update Notification")
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { notificationViewModel.cancelNotification() }) {
            Text(text = "Cancel Notification")
        }
    }
}
