package com.jn.echomaze.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.ui.components.IconButtonGlow
import com.jn.echomaze.ui.components.glow
import com.jn.echomaze.ui.theme.CyberCyan
import com.jn.echomaze.ui.theme.EchoMazeTheme
import com.jn.echomaze.ui.theme.GoldCoin
import com.jn.echomaze.ui.theme.NeonPurple
import com.jn.echomaze.ui.theme.PressStart2P

@Composable
fun LevelCompleteScreen(
    levelId: Int,
    score: Int,
    coinsEarned: Int,
    onNextLevelClick: () -> Unit,
    onReplayClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = "LEVEL\nCOMPLETE!",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 4.sp,
                    lineHeight = 50.sp,
                    fontFamily = PressStart2P
                ),
                color = CyberCyan,
                modifier = Modifier.glow(CyberCyan, alpha = 0.5f)
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .glow(NeonPurple, alpha = 0.2f, borderRadius = 8.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                border = androidx.compose.foundation.BorderStroke(2.dp, NeonPurple.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(label = "TIME", value = "45s") // Placeholder
                        StatItem(label = "COINS EARNED", value = "+$coinsEarned", valueColor = GoldCoin)
                    }

                    // Score Display
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "TOTAL SCORE:",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = PressStart2P),
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = score.toString(),
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = PressStart2P
                            ),
                            color = Color.White
                        )
                    }

                    // Stars
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        repeat(3) {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = null,
                                tint = GoldCoin,
                                modifier = Modifier.size(48.dp).glow(GoldCoin, alpha = 0.3f)
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButtonGlow(
                    icon = Icons.Rounded.Replay,
                    contentDescription = "Replay",
                    onClick = onReplayClick,
                    color = NeonPurple,
                    modifier = Modifier.size(64.dp)
                )
                
                IconButtonGlow(
                    icon = Icons.AutoMirrored.Rounded.NavigateNext,
                    contentDescription = "Next Level",
                    onClick = onNextLevelClick,
                    color = CyberCyan,
                    modifier = Modifier.size(80.dp)
                )
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, valueColor: Color = Color.White) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = PressStart2P,
                fontSize = 10.sp
            ),
            color = Color.White.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = PressStart2P
            ),
            color = valueColor
        )
    }
}

@Preview
@Composable
fun LevelCompletePreview() {
    EchoMazeTheme {
        LevelCompleteScreen(1, 9800, 50, {}, {})
    }
}
