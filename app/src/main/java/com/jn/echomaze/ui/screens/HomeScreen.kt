package com.jn.echomaze.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Leaderboard
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.ui.components.HeaderBar
import com.jn.echomaze.ui.components.HeaderVariant
import com.jn.echomaze.ui.components.NeonButton
import com.jn.echomaze.ui.components.NeonScaffold
import com.jn.echomaze.ui.components.NeonTitle
import com.jn.echomaze.ui.theme.AppTheme
import com.jn.echomaze.ui.theme.CyberCyan
import com.jn.echomaze.ui.theme.GoldCoin
import com.jn.echomaze.ui.theme.NeonPurple
import com.jn.echomaze.ui.viewmodel.home.HomeEvent
import com.jn.echomaze.ui.viewmodel.home.HomeUiState

@Composable
fun HomeScreen(
    state: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    onPlayClick: () -> Unit,
    onDailyChallengeClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onHelpClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onShopClick: () -> Unit
) {
    NeonScaffold(
        topBar = {
            HeaderBar(
                variant = HeaderVariant.HOME,
                coinBalance = state.coinBalance,
                onShopClick = onShopClick
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                NeonTitle(text = "PuzzleMaze", fontSize = 44.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "SLIDE MASTER",
                    color = CyberCyan,
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 4.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HomeMenuItem(
                    "PLAY GAME",
                    onPlayClick,
                    CyberCyan,
                    Icons.Rounded.PlayArrow,
                    isPrimary = true
                )
                HomeMenuItem(
                    "DAILY CHALLENGE",
                    onDailyChallengeClick,
                    GoldCoin,
                    Icons.Rounded.EmojiEvents
                )
                HomeMenuItem(
                    "LEADERBOARD",
                    onLeaderboardClick,
                    NeonPurple,
                    Icons.Rounded.Leaderboard
                )
                HomeMenuItem("SHOP", onShopClick, Color.Yellow, Icons.Rounded.ShoppingBag)
                HomeMenuItem("HELP", onHelpClick, Color.White, Icons.AutoMirrored.Rounded.Help)
                HomeMenuItem("SETTINGS", onSettingsClick, Color.Gray, Icons.Rounded.Settings)
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun HomeMenuItem(
    text: String,
    onClick: () -> Unit,
    color: Color,
    icon: ImageVector,
    isPrimary: Boolean = false
) {
    NeonButton(
        text = text,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = color,
        isPrimary = isPrimary,
        icon = icon
    )
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun HomePreview() {
    AppTheme {
        HomeScreen(HomeUiState(coinBalance = 100), {}, {}, {}, {}, {}, {}, {})
    }
}
