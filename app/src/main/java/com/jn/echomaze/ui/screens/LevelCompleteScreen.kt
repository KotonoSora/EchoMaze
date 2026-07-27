package com.jn.echomaze.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.automirrored.rounded.NavigateNext
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.domain.model.UserStats
import com.jn.echomaze.ui.components.IconButtonGlow
import com.jn.echomaze.ui.components.NeonCard
import com.jn.echomaze.ui.components.NeonScaffold
import com.jn.echomaze.ui.components.NeonTitle
import com.jn.echomaze.ui.components.glow
import com.jn.echomaze.ui.theme.AppTheme
import com.jn.echomaze.ui.theme.CyberCyan
import com.jn.echomaze.ui.theme.GoldCoin
import com.jn.echomaze.ui.theme.NeonPurple
import com.jn.echomaze.ui.theme.PressStart2P
import kotlinx.coroutines.delay

@Composable
fun LevelCompleteScreen(
    levelId: Int,
    score: Int,
    coinsEarned: Int,
    stars: Int,
    timeSeconds: Int,
    onNextLevelClick: () -> Unit,
    onReplayClick: () -> Unit,
    stats: UserStats? = null
) {
    val xpEarned = 100 + (stars * 20)
    val xp = stats?.totalXp ?: 0
    val xpBefore = (xp - xpEarned).coerceAtLeast(0)
    val xpInLevelBefore = xpBefore % 1000
    val xpInLevelAfter = xp % 1000
    val progressBefore = xpInLevelBefore / 1000f
    val progressAfter = xpInLevelAfter / 1000f

    val animatedProgress = remember { Animatable(progressBefore) }
    val animatedStars = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(500)
        animatedStars.animateTo(
            targetValue = stars.toFloat(),
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
        animatedProgress.animateTo(
            targetValue = progressAfter,
            animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
        )
    }

    NeonScaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Spacer(modifier = Modifier.weight(weight = 0.1f))

            NeonTitle(text = "LEVEL COMPLETE!", color = CyberCyan, fontSize = 24.sp)

            NeonCard(color = NeonPurple, modifier = Modifier.fillMaxWidth()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(label = "TIME", value = "${timeSeconds}s")
                        StatItem(
                            label = "XP EARNED",
                            value = "+${100 + (stars * 20)}",
                            valueColor = CyberCyan
                        )
                        StatItem(
                            label = "COINS",
                            value = "+$coinsEarned",
                            valueColor = GoldCoin
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "TOTAL SCORE:",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = score.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        repeat(3) { index ->
                            val isFilled = animatedStars.value >= (index + 1)
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = null,
                                tint = if (isFilled) GoldCoin else Color.White.copy(alpha = 0.2f),
                                modifier = Modifier
                                    .size(48.dp)
                                    .glow(if (isFilled) GoldCoin else Color.Transparent, alpha = 0.3f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "XP PROGRESS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 8.sp,
                                fontFamily = PressStart2P
                            ),
                            color = CyberCyan
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { animatedProgress.value },
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(8.dp)
                                .clip(MaterialTheme.shapes.small),
                            color = CyberCyan,
                            trackColor = Color.White.copy(alpha = 0.1f),
                            strokeCap = StrokeCap.Round
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(weight = 1f))

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

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, valueColor: Color = Color.White) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = Color.White.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = valueColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun LevelCompletePreview() {
    AppTheme {
        LevelCompleteScreen(1, 9800, 50, 3, 45, {}, {})
    }
}
