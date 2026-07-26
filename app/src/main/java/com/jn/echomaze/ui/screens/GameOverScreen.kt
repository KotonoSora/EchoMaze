package com.jn.echomaze.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.ui.components.IconButtonGlow
import com.jn.echomaze.ui.components.NeonScaffold
import com.jn.echomaze.ui.components.NeonTitle
import com.jn.echomaze.ui.theme.AppTheme

@Composable
fun GameOverScreen(
    onReplayClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    NeonScaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NeonTitle(text = "GAME OVER", color = Color.Red, fontSize = 40.sp)

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "The sequence was broken...",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
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
    AppTheme {
        GameOverScreen({}, {})
    }
}
