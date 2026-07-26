package com.jn.echomaze.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddShoppingCart
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Leaderboard
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Redeem
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.jn.echomaze.ui.components.IconButtonGlow
import com.jn.echomaze.ui.components.NeonButton
import com.jn.echomaze.ui.components.NeonScaffold
import com.jn.echomaze.ui.components.NeonTitle
import com.jn.echomaze.ui.components.glow
import com.jn.echomaze.ui.theme.AppTheme
import com.jn.echomaze.ui.theme.CyberCyan
import com.jn.echomaze.ui.theme.ElectricBlue
import com.jn.echomaze.ui.theme.GoldCoin
import com.jn.echomaze.ui.theme.NeonPurple

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
    onShopClick: () -> Unit
) {
    NeonScaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlayerProfileBar(stats)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Coin Counter
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                            .border(1.dp, GoldCoin.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = coinBalance.toString(),
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Rounded.MonetizationOn,
                            contentDescription = null,
                            tint = GoldCoin,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Shop Button
                    IconButtonGlow(
                        icon = Icons.Rounded.AddShoppingCart,
                        contentDescription = "Open Shop",
                        onClick = onShopClick,
                        modifier = Modifier.size(44.dp),
                        color = GoldCoin
                    )
                }
            }
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    NeonButton(
                        text = "SKINS",
                        onClick = onSkinsShopClick,
                        modifier = Modifier.weight(1f),
                        color = ElectricBlue,
                        icon = Icons.Rounded.Palette
                    )
                    NeonButton(
                        text = "HELP",
                        onClick = onHelpClick,
                        modifier = Modifier.weight(1f),
                        color = Color.White,
                        icon = Icons.Rounded.QuestionMark
                    )
                }

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
    }
}

@Composable
fun PlayerProfileBar(stats: UserStats?) {
    val xp = stats?.totalXp ?: 0
    val level = (xp / 1000) + 1
    val xpInLevel = xp % 1000
    val progress = xpInLevel / 1000f

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(NeonPurple)
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "L$level",
                color = Color.White,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        if ((stats?.loginStreak ?: 0) > 1) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
                    .border(1.dp, GoldCoin, RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = "🔥", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${stats?.loginStreak}",
                    color = GoldCoin,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
        }

        Column {
            Text(
                text = "LEVEL UP",
                color = Color.White.copy(alpha = 0.5f),
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.sp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .width(70.dp)
                    .height(6.dp)
                    .clip(CircleShape),
                color = CyberCyan,
                trackColor = Color.White.copy(alpha = 0.2f),
            )
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
