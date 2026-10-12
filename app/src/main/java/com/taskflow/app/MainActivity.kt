package com.taskflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.taskflow.app.di.DefaultAppContainer
import com.taskflow.app.ui.navigation.TaskFlowNavHost
import com.taskflow.app.ui.theme.TaskFlowTheme

class MainActivity : ComponentActivity() {

    private lateinit var appContainer: DefaultAppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContainer = DefaultAppContainer(applicationContext)

        setContent {
            TaskFlowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaskFlowNavHost(
                        taskRepository = appContainer.taskRepository
                    )
                }
            }
        }
    }
}
