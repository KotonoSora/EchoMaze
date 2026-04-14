package com.kotonosora.echomaze.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotonosora.echomaze.ui.components.IconButtonGlow
import com.kotonosora.echomaze.ui.components.glow
import com.kotonosora.echomaze.ui.theme.EchoMazeTheme
import com.kotonosora.echomaze.ui.theme.PressStart2P

@Composable
fun GameOverScreen(
    onReplayClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f))
            .systemBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            Text(
                text = "GAME OVER",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 4.sp,
                    fontFamily = PressStart2P
                ),
                color = Color.Red,
                modifier = Modifier.glow(Color.Red, alpha = 0.5f)
            )

            Text(
                text = "The maze remains hidden...",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = PressStart2P,
                    letterSpacing = 1.sp,
                    lineHeight = 24.sp
                ),
                color = Color.White.copy(alpha = 0.7f)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButtonGlow(
                    icon = Icons.Rounded.Close,
                    contentDescription = "Menu",
                    onClick = onMenuClick,
                    color = Color.Gray,
                    modifier = Modifier.size(64.dp)
                )
                
                IconButtonGlow(
                    icon = Icons.Rounded.Replay,
                    contentDescription = "Try Again",
                    onClick = onReplayClick,
                    color = Color.White,
                    modifier = Modifier.size(80.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun GameOverPreview() {
    EchoMazeTheme {
        GameOverScreen({}, {})
    }
}
