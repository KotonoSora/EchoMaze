package com.jn.echomaze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jn.echomaze.ui.navigation.EchoMazeApp
import com.jn.echomaze.ui.theme.EchoMazeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EchoMazeTheme {
                EchoMazeApp()
            }
        }
    }
}
