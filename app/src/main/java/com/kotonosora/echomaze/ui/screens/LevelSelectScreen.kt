package com.kotonosora.echomaze.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotonosora.echomaze.data.LevelEntity
import com.kotonosora.echomaze.ui.components.IconButtonGlow
import com.kotonosora.echomaze.ui.components.glow
import com.kotonosora.echomaze.ui.theme.CyberCyan
import com.kotonosora.echomaze.ui.theme.EchoMazeTheme
import com.kotonosora.echomaze.ui.theme.GoldCoin
import com.kotonosora.echomaze.ui.theme.NeonPurple
import com.kotonosora.echomaze.ui.theme.PressStart2P

@Composable
fun LevelSelectScreen(
    levels: List<LevelEntity>,
    onLevelClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
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
                text = "SELECT\nLEVEL",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    fontFamily = PressStart2P
                ),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(levels) { level ->
                LevelItem(level = level, onClick = { if (level.isUnlocked) onLevelClick(level.id) })
            }
        }
    }
}

@Composable
fun LevelItem(level: LevelEntity, onClick: () -> Unit) {
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
                            tint = if (index < level.starsEarned) GoldCoin else Color.Gray.copy(alpha = 0.5f),
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
    EchoMazeTheme {
        LevelSelectScreen(
            levels = listOf(
                LevelEntity(1, 1, 3, true),
                LevelEntity(2, 2, 2, true),
                LevelEntity(3, 3, 0, true),
                LevelEntity(4, 4, 0, false),
                LevelEntity(5, 5, 0, false)
            ),
            onLevelClick = {},
            onBackClick = {}
        )
    }
}
