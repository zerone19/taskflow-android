package com.taskflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.taskflow.app.data.local.ThemePreferences
import com.taskflow.app.di.DefaultAppContainer
import com.taskflow.app.ui.navigation.TaskFlowNavHost
import com.taskflow.app.ui.theme.TaskFlowTheme
import com.taskflow.app.worker.ExpiredTaskWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private lateinit var appContainer: DefaultAppContainer
    private lateinit var themePreferences: ThemePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContainer = DefaultAppContainer(applicationContext)
        themePreferences = ThemePreferences(applicationContext)

        // Schedule periodic expired task cleanup (setiap 1 jam).
        val workRequest = PeriodicWorkRequestBuilder<ExpiredTaskWorker>(1, TimeUnit.HOURS).build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            ExpiredTaskWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        setContent {
            val isDarkModePref by themePreferences.isDarkMode.collectAsState(initial = null)
            val systemDark = isSystemInDarkTheme()
            val darkTheme = isDarkModePref ?: systemDark

            TaskFlowTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaskFlowNavHost(
                        taskRepository = appContainer.taskRepository,
                        subtaskRepository = appContainer.subtaskRepository,
                        isDarkMode = darkTheme,
                        onToggleTheme = { enabled ->
                            kotlinx.coroutines.MainScope().launch {
                                themePreferences.setDarkMode(enabled)
                            }
                        }
                    )
                }
            }
        }
    }
}
