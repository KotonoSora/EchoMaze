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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jn.echomaze.domain.model.UserStats
import com.jn.echomaze.ui.components.HeaderBar
import com.jn.echomaze.ui.components.HeaderVariant
import com.jn.echomaze.ui.components.NeonCard
import com.jn.echomaze.ui.components.NeonScaffold
import com.jn.echomaze.ui.theme.AppTheme
import com.jn.echomaze.ui.theme.CyberCyan
import com.jn.echomaze.ui.theme.NeonPurple

@Composable
fun SettingsScreen(
    stats: UserStats?,
    coinBalance: Int,
    onBackClick: () -> Unit
) {
    var soundEnabled by remember { mutableStateOf(true) }
    var vibrationEnabled by remember { mutableStateOf(true) }

    NeonScaffold(
        topBar = {
            HeaderBar(
                variant = HeaderVariant.STANDARD,
                title = "SETTINGS",
                coinBalance = coinBalance,
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SettingToggle(
                    title = "Sound Effects",
                    icon = Icons.Rounded.VolumeUp,
                    checked = soundEnabled,
                    onCheckedChange = { soundEnabled = it },
                    color = CyberCyan
                )
                SettingToggle(
                    title = "Vibration",
                    icon = Icons.Rounded.GraphicEq,
                    checked = vibrationEnabled,
                    onCheckedChange = { vibrationEnabled = it },
                    color = NeonPurple
                )
            }

            Spacer(modifier = Modifier.weight(weight = 1f))

            NeonCard(color = CyberCyan, modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "STATISTICS",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White.copy(alpha = 0.5f)
                    )

                    if (stats != null) {
                        val minutes = (stats.totalPlayTimeMillis / 60000) % 60
                        val hours = (stats.totalPlayTimeMillis / 3600000)
                        val formattedTime =
                            if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"

                        StatRow("Playtime", formattedTime)
                        StatRow("Levels", stats.totalLevelsCompleted.toString())
                        StatRow("Moves", stats.totalMovesMade.toString())
                        StatRow("XP", stats.totalXp.toString())
                        StatRow("Coins", stats.totalCoinsCollected.toString())
                    } else {
                        StatRow("Loading...", "")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SettingToggle(
    title: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    color: Color
) {
    NeonCard(color = color, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = title, style = MaterialTheme.typography.bodyMedium, color = Color.White)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = color,
                    checkedTrackColor = color.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.8f),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = value,
            color = CyberCyan,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
fun SettingsPreview() {
    AppTheme {
        SettingsScreen(
            stats = UserStats(3600000, 10, 50, 1000, 500, 2500, 0, 0),
            coinBalance = 500,
            onBackClick = {}
        )
    }
}
