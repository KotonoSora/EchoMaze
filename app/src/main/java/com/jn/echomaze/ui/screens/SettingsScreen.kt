package com.jn.echomaze.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.data.StatsEntity
import com.jn.echomaze.ui.components.IconButtonGlow
import com.jn.echomaze.ui.components.glow
import com.jn.echomaze.ui.theme.CyberCyan
import com.jn.echomaze.ui.theme.EchoMazeTheme
import com.jn.echomaze.ui.theme.NeonPurple

@Composable
fun SettingsScreen(
    stats: StatsEntity?,
    onBackClick: () -> Unit
) {
    var soundEnabled by remember { mutableStateOf(true) }
    var vibrationEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButtonGlow(
                icon = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                onClick = onBackClick
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "SETTINGS & STATS",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

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

        Spacer(modifier = Modifier.weight(1f))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .glow(CyberCyan, alpha = 0.1f, borderRadius = 24.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
        ) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "STATISTICS", style = MaterialTheme.typography.labelLarge, color = Color.White.copy(alpha = 0.5f))
                
                if (stats != null) {
                    val minutes = (stats.totalPlayTimeMillis / 60000) % 60
                    val hours = (stats.totalPlayTimeMillis / 3600000)
                    val formattedTime = if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
                    
                    StatRow("Playtime", formattedTime)
                    StatRow("Levels Completed", stats.totalLevelsCompleted.toString())
                    StatRow("Total Pulses", stats.totalPulsesUsed.toString())
                    StatRow("Total Coins Earned", stats.totalCoinsCollected.toString())
                    StatRow("Total Coins Spent", stats.totalCoinsSpent.toString())
                } else {
                    StatRow("Loading...", "")
                }
            }
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
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = title, style = MaterialTheme.typography.bodyLarge, color = Color.White)
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
        Text(text = label, color = Color.White.copy(alpha = 0.8f))
        Text(text = value, color = CyberCyan, fontWeight = FontWeight.Bold)
    }
}

@Preview
@Composable
fun SettingsPreview() {
    EchoMazeTheme {
        SettingsScreen(
            stats = StatsEntity(1, 3600000, 10, 50, 1000, 500),
            onBackClick = {}
        )
    }
}
