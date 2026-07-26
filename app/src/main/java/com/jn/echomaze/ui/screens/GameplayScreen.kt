package com.jn.echomaze.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.ui.components.IconButtonGlow
import com.jn.echomaze.ui.components.NeonCard
import com.jn.echomaze.ui.components.NeonScaffold
import com.jn.echomaze.ui.theme.AppTheme
import com.jn.echomaze.ui.theme.CyberCyan
import com.jn.echomaze.ui.theme.GoldCoin

@Composable
fun GameplayScreen(
    levelId: Int,
    coins: Int,
    movesCount: Int,
    maxMoves: Int,
    gridSize: Int,
    tiles: List<Int>,
    onTileClick: (Int) -> Unit,
    onPauseClick: () -> Unit,
    onQuickBuyCoins: () -> Unit
) {
    NeonScaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (levelId == 0) "DAILY" else "LEVEL",
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = if (levelId == 0) "CHALLENGE" else levelId.toString()
                            .padStart(2, '0'),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Quick Buy Coins
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                            .border(1.dp, GoldCoin.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .clickable { onQuickBuyCoins() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "$coins",
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Buy Coins",
                            tint = GoldCoin,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    IconButtonGlow(
                        icon = Icons.Rounded.Pause,
                        contentDescription = "Pause",
                        onClick = onPauseClick,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Puzzle Grid
            BoxWithConstraints(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                val spacing = 8.dp
                val boardSize = maxWidth
                val tileSize = (boardSize - (spacing * (gridSize + 1))) / gridSize

                Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
                    for (r in 0 until gridSize) {
                        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                            for (c in 0 until gridSize) {
                                val index = r * gridSize + c
                                Box(modifier = Modifier.size(tileSize)) {
                                    if (index < tiles.size) {
                                        val tileId = tiles[index]
                                        PuzzleTile(
                                            tileId = tileId,
                                            onClick = { onTileClick(index) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Stats - Bottom
            NeonCard(color = CyberCyan, modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "MOVES",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "$movesCount / $maxMoves",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (movesCount > maxMoves * 0.8f) Color.Red else CyberCyan,
                            fontSize = 12.sp
                        )
                    }

                    VerticalDivider(
                        modifier = Modifier.height(40.dp),
                        color = Color.White.copy(alpha = 0.1f)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "TARGET",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "${gridSize * gridSize * 10}",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun PuzzleTile(tileId: Int, onClick: () -> Unit) {
    if (tileId == 0) {
        Box(modifier = Modifier.fillMaxSize())
    } else {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .clickable { onClick() },
            color = CyberCyan.copy(alpha = 0.8f),
            border = BorderStroke(2.dp, CyberCyan)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tileId.toString(),
                    color = Color.Black,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameplayScreenPreview() {
    AppTheme {
        GameplayScreen(
            levelId = 1,
            coins = 150,
            movesCount = 5,
            maxMoves = 50,
            gridSize = 3,
            tiles = listOf(1, 2, 3, 4, 5, 6, 7, 8, 0),
            onTileClick = {},
            onPauseClick = {},
            onQuickBuyCoins = {}
        )
    }
}
