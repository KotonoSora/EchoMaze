package com.jn.echomaze.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.jn.echomaze.ui.components.NeonScaffold
import com.jn.echomaze.ui.theme.AppTheme
import com.jn.echomaze.ui.theme.CyberCyan
import com.jn.echomaze.ui.viewmodel.settings.SettingsEvent
import com.jn.echomaze.ui.viewmodel.settings.SettingsUiState

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onEvent: (SettingsEvent) -> Unit,
    onBackClick: () -> Unit
) {
    NeonScaffold(
        topBar = {
            HeaderBar(
                variant = HeaderVariant.STANDARD,
                title = "SETTINGS",
                coinBalance = state.coinBalance,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            SectionHeader(icon = Icons.Rounded.Settings, title = "PREFERENCES")

            SettingToggle(
                label = "Sound Effects",
                checked = state.stats?.isSoundEnabled ?: true,
                onCheckedChange = { onEvent(SettingsEvent.OnSoundToggled(it)) }
            )
            SettingToggle(
                label = "Background Music",
                checked = state.stats?.isMusicEnabled ?: true,
                onCheckedChange = { onEvent(SettingsEvent.OnMusicToggled(it)) }
            )
        }
    }
}

@Composable
private fun SectionHeader(icon: ImageVector, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        Icon(icon, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            color = CyberCyan,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
    }
}

@Composable
private fun SettingToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Color.White, style = MaterialTheme.typography.bodyLarge)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = CyberCyan,
                checkedTrackColor = CyberCyan.copy(alpha = 0.5f),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.DarkGray
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    AppTheme {
        SettingsScreen(SettingsUiState(coinBalance = 100), {}, {})
    }
}
