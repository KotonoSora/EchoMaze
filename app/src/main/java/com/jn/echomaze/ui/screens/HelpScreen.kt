package com.jn.echomaze.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.ui.components.HeaderBar
import com.jn.echomaze.ui.components.HeaderVariant
import com.jn.echomaze.ui.components.NeonButton
import com.jn.echomaze.ui.components.NeonCard
import com.jn.echomaze.ui.components.NeonScaffold
import com.jn.echomaze.ui.theme.AppTheme
import com.jn.echomaze.ui.theme.CyberCyan
import com.jn.echomaze.ui.theme.GoldCoin
import com.jn.echomaze.ui.theme.NeonPurple

@Composable
fun HelpScreen(
    coinBalance: Int,
    onBackClick: () -> Unit
) {
    NeonScaffold(
        topBar = {
            HeaderBar(
                variant = HeaderVariant.STANDARD,
                title = "HOW TO PLAY",
                coinBalance = coinBalance,
                onBackClick = onBackClick
            )
        }
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HelpSection(
                icon = Icons.Rounded.TouchApp,
                title = "MOVEMENT",
                description = "Tap a tile adjacent to the empty slot to slide it. Only one tile can move at a time.",
                color = CyberCyan
            )

            Spacer(modifier = Modifier.height(24.dp))

            HelpSection(
                icon = Icons.Rounded.Info,
                title = "OBJECTIVE",
                description = "Arrange all numbered tiles in numerical order from left to right, top to bottom. The empty slot should be at the very end.",
                color = NeonPurple
            )

            Spacer(modifier = Modifier.height(24.dp))

            HelpSection(
                icon = Icons.Rounded.MonetizationOn,
                title = "REWARDS",
                description = "Complete puzzles in fewer moves to earn more stars, coins, and XP!",
                color = GoldCoin
            )

            Spacer(modifier = Modifier.height(32.dp))

            NeonButton(
                text = "GOT IT!",
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth(),
                color = CyberCyan
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun HelpSection(
    icon: ImageVector,
    title: String,
    description: String,
    color: Color
) {
    NeonCard(color = color, modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = color
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HelpScreenPreview() {
    AppTheme {
        HelpScreen(coinBalance = 500, onBackClick = {})
    }
}
