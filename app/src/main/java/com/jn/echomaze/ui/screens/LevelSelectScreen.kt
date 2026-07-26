package com.jn.echomaze.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jn.echomaze.domain.model.Level
import com.jn.echomaze.ui.components.HeaderBar
import com.jn.echomaze.ui.components.HeaderVariant
import com.jn.echomaze.ui.components.glow
import com.jn.echomaze.ui.theme.AppTheme
import com.jn.echomaze.ui.theme.CyberCyan
import com.jn.echomaze.ui.theme.GoldCoin
import com.jn.echomaze.ui.theme.PressStart2P

@Composable
fun LevelSelectScreen(
    levels: List<Level>,
    coinBalance: Int,
    onLevelClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HeaderBar(
            variant = HeaderVariant.STANDARD,
            title = "SELECT LEVEL",
            coinBalance = coinBalance,
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(32.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            items(items = levels, key = { level -> level.id }) { level ->
                LevelItem(level = level, onClick = { if (level.isUnlocked) onLevelClick(level.id) })
            }
        }
    }
}

@Composable
fun LevelItem(level: Level, onClick: () -> Unit) {
    val borderColor = if (level.isUnlocked) CyberCyan else Color.Gray.copy(alpha = 0.5f)
    val glowColor = if (level.isUnlocked) CyberCyan else Color.Transparent

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .glow(glowColor, alpha = 0.2f, borderRadius = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(enabled = level.isUnlocked, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (level.isUnlocked) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = level.levelNumber.toString().padStart(2, '0'),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = PressStart2P
                    ),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    repeat(3) { index ->
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = null,
                            tint = if (index < level.starsEarned) GoldCoin else Color.Gray.copy(
                                alpha = 0.5f
                            ),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        } else {
            Icon(
                imageVector = Icons.Rounded.Lock,
                contentDescription = "Locked",
                tint = Color.Gray.copy(alpha = 0.5f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LevelSelectPreview() {
    AppTheme {
        LevelSelectScreen(
            levels = listOf(
                Level(1, 1, 3, true),
                Level(2, 2, 2, true),
                Level(3, 3, 0, true),
                Level(4, 4, 0, false),
                Level(5, 5, 0, false)
            ),
            coinBalance = 500,
            onLevelClick = {},
            onBackClick = {}
        )
    }
}
