package com.jn.echomaze.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Leaderboard
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Redeem
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.domain.model.UserStats
import com.jn.echomaze.ui.components.HeaderBar
import com.jn.echomaze.ui.components.HeaderVariant
import com.jn.echomaze.ui.components.LevelUpDialog
import com.jn.echomaze.ui.components.NeonButton
import com.jn.echomaze.ui.components.NeonCard
import com.jn.echomaze.ui.components.NeonScaffold
import com.jn.echomaze.ui.components.NeonTitle
import com.jn.echomaze.ui.components.glow
import com.jn.echomaze.ui.theme.AppTheme
import com.jn.echomaze.ui.theme.CyberCyan
import com.jn.echomaze.ui.theme.ElectricBlue
import com.jn.echomaze.ui.theme.GoldCoin
import com.jn.echomaze.ui.theme.NeonPurple
import com.jn.echomaze.ui.theme.PressStart2P

@Composable
fun HomeScreen(
    coinBalance: Int,
    stats: UserStats?,
    canClaimDailyReward: Boolean,
    onClaimDailyReward: () -> Unit,
    onPlayClick: () -> Unit,
    onDailyChallengeClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onSkinsShopClick: () -> Unit,
    onHelpClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onShopClick: () -> Unit,
    levelUpToCelebrate: Int? = null,
    onDismissLevelUp: () -> Unit = {}
) {
    NeonScaffold(
        topBar = {
            HeaderBar(
                variant = HeaderVariant.HOME,
                coinBalance = coinBalance,
                stats = stats,
                onShopClick = onShopClick
            )
        }
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Logo / App Name
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                NeonTitle(text = "PuzzleMaze", fontSize = 36.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "SLIDE MASTER",
                    color = CyberCyan,
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 4.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.glow(CyberCyan, alpha = 0.3f)
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            // Options
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (canClaimDailyReward) {
                    DailyRewardButton(onClaimDailyReward)
                }

                if ((stats?.loginStreak ?: 0) > 1) {
                    StreakDisplay(streak = stats?.loginStreak ?: 0)
                }

                NeonButton(
                    text = "PLAY GAME",
                    onClick = onPlayClick,
                    modifier = Modifier.fillMaxWidth(),
                    color = CyberCyan,
                    icon = Icons.Rounded.PlayArrow
                )

                NeonButton(
                    text = "DAILY CHALLENGE",
                    onClick = onDailyChallengeClick,
                    modifier = Modifier.fillMaxWidth(),
                    color = GoldCoin,
                    icon = Icons.Rounded.EmojiEvents
                )

                NeonButton(
                    text = "LEADERBOARD",
                    onClick = onLeaderboardClick,
                    modifier = Modifier.fillMaxWidth(),
                    color = NeonPurple,
                    icon = Icons.Rounded.Leaderboard
                )

                NeonButton(
                    text = "SKINS",
                    onClick = onSkinsShopClick,
                    modifier = Modifier.fillMaxWidth(),
                    color = ElectricBlue,
                    icon = Icons.Rounded.Palette
                )

                NeonButton(
                    text = "HELP",
                    onClick = onHelpClick,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    icon = Icons.Rounded.QuestionMark
                )

                NeonButton(
                    text = "SETTINGS",
                    onClick = onSettingsClick,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Gray,
                    icon = Icons.Rounded.Settings
                )
            }

            Spacer(modifier = Modifier.height(64.dp))
        }

        levelUpToCelebrate?.let { level ->
            LevelUpDialog(newLevel = level, onDismiss = onDismissLevelUp)
        }
    }
}

@Composable
fun StreakDisplay(streak: Int) {
    NeonCard(
        color = GoldCoin,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "🔥", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "$streak DAY STREAK",
                    color = GoldCoin,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = PressStart2P,
                        fontSize = 12.sp
                    )
                )
                Text(
                    text = "Keep it up for more rewards!",
                    color = Color.White.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp
                )
            }
        }
    }
}

@Composable
fun DailyRewardButton(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .glow(GoldCoin, alpha = 0.4f, borderRadius = 28.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(GoldCoin)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Redeem, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "CLAIM 50 COINS!",
                color = Color.Black,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun HomePreview() {
    AppTheme {
        HomeScreen(120, null, true, {}, {}, {}, {}, {}, {}, {}, {})
    }
}
