package com.jn.echomaze.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.ui.components.GlowButton
import com.jn.echomaze.ui.components.NeonScaffold
import com.jn.echomaze.ui.components.NeonTitle
import com.jn.echomaze.ui.theme.AppTheme
import com.jn.echomaze.ui.theme.CyberCyan
import com.jn.echomaze.ui.theme.NeonPurple

@Composable
fun GameOverScreen(
    onReplayClick: () -> Unit,
    onPlayNewClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    BackHandler {
        onMenuClick()
    }

    NeonScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NeonTitle(
                text = "GAME OVER",
                color = Color.Red,
                fontSize = 40.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "The sequence was broken...",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GlowButton(
                    text = "Try Again (30)",
                    onClick = onReplayClick,
                    modifier = Modifier.width(280.dp),
                    color = CyberCyan
                )

                GlowButton(
                    text = "Play New",
                    onClick = onPlayNewClick,
                    modifier = Modifier.width(280.dp),
                    color = NeonPurple
                )

                GlowButton(
                    text = "Home Screen",
                    onClick = onMenuClick,
                    modifier = Modifier.width(280.dp),
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview
@Composable
fun GameOverPreview() {
    AppTheme {
        GameOverScreen({}, {}, {})
    }
}
