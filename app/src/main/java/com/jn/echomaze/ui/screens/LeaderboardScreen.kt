package com.jn.echomaze.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.Stars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.domain.model.Achievement
import com.jn.echomaze.domain.model.GameHistory
import com.jn.echomaze.ui.components.IconButtonGlow
import com.jn.echomaze.ui.components.NeonCard
import com.jn.echomaze.ui.components.NeonScaffold
import com.jn.echomaze.ui.components.NeonTitle
import com.jn.echomaze.ui.theme.AppTheme
import com.jn.echomaze.ui.theme.CyberCyan
import com.jn.echomaze.ui.theme.GoldCoin
import com.jn.echomaze.ui.theme.NeonPurple
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun LeaderboardScreen(
    onBackClick: () -> Unit,
    topScores: List<GameHistory>,
    myHistory: List<GameHistory>,
    achievements: List<Achievement>
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("SCORES", "HISTORY", "BADGES")

    NeonScaffold(
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButtonGlow(
                        icon = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back",
                        onClick = onBackClick
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    NeonTitle(text = "STATISTICS", fontSize = 20.sp)
                }

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = CyberCyan,
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = CyberCyan
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontSize = 10.sp
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { _ ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    if (topScores.isEmpty()) {
                        item { EmptyState("NO SCORES YET") }
                    } else {
                        topScores.forEach { record ->
                            item { HistoryItem(record) }
                        }
                    }
                }

                1 -> {
                    if (myHistory.isEmpty()) {
                        item { EmptyState("NO HISTORY YET") }
                    } else {
                        myHistory.forEach { record ->
                            item { HistoryItem(record) }
                        }
                    }
                }

                2 -> {
                    achievements.forEach { achievement ->
                        item { AchievementItem(achievement) }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp), contentAlignment = Alignment.Center
    ) {
        Text(text = message, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun AchievementItem(achievement: Achievement) {
    val color = if (achievement.isUnlocked) GoldCoin else Color.Gray

    NeonCard(color = color, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Stars,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = achievement.title,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (achievement.isUnlocked) Color.White else Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 8.sp,
                    lineHeight = 12.sp
                )
            }
            if (achievement.isUnlocked) {
                Text(text = "UNLOCKED", color = GoldCoin, fontSize = 8.sp)
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = achievement.rewardCoins.toString(),
                        color = Color.White,
                        fontSize = 8.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Rounded.MonetizationOn,
                        null,
                        tint = GoldCoin,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryItem(record: GameHistory) {
    val configuration = LocalConfiguration.current
    val locale = configuration.locales[0]
    val sdf = remember(locale) { SimpleDateFormat("dd/MM HH:mm", locale) }
    val dateStr = sdf.format(Date(record.timestamp))

    NeonCard(
        color = if (record.isDailyChallenge) GoldCoin else NeonPurple,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (record.isDailyChallenge) "DAILY CHALLENGE" else "LEVEL ${record.levelId}",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (record.isDailyChallenge) GoldCoin else CyberCyan
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dateStr,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${record.score} PTS",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "+${record.rewardCoins}",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldCoin
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Rounded.MonetizationOn,
                        contentDescription = null,
                        tint = GoldCoin,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardScreenPreview() {
    AppTheme {
        LeaderboardScreen(
            onBackClick = {},
            topScores = listOf(
                GameHistory(1, System.currentTimeMillis(), 1200, 30, 50, 1),
                GameHistory(2, System.currentTimeMillis(), 1500, 45, 100, 2, true)
            ),
            myHistory = listOf(
                GameHistory(3, System.currentTimeMillis(), 800, 25, 30, 1)
            ),
            achievements = listOf(
                Achievement("1", "First Win", "Complete your first level", true),
                Achievement("2", "Pro Slider", "Complete 10 levels", false)
            )
        )
    }
}
