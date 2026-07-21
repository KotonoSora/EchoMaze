package com.jn.echomaze.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.ui.components.GlowButton
import com.jn.echomaze.ui.theme.EchoMazeTheme
import com.jn.echomaze.ui.theme.NeonPurple

@Composable
fun PauseScreen(
    onResumeClick: () -> Unit,
    onMenuClick: () -> Unit,
    onRestartClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f))
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "PAUSED",
                style = MaterialTheme.typography.displayMedium.copy(
                    letterSpacing = 4.sp
                ),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            GlowButton(
                text = "Resume",
                onClick = onResumeClick,
                modifier = Modifier.width(200.dp)
            )

            GlowButton(
                text = "Restart",
                onClick = onRestartClick,
                modifier = Modifier.width(200.dp),
                color = NeonPurple
            )

            GlowButton(
                text = "Main Menu",
                onClick = onMenuClick,
                modifier = Modifier.width(200.dp),
                color = Color.Gray
            )
        }
    }
}

@Preview
@Composable
fun PausePreview() {
    EchoMazeTheme {
        PauseScreen({}, {}, {})
    }
}