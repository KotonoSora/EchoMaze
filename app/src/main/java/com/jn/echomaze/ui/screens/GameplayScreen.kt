package com.jn.echomaze.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.ui.components.HeaderBar
import com.jn.echomaze.ui.components.HeaderVariant
import com.jn.echomaze.ui.components.NeonButton
import com.jn.echomaze.ui.components.NeonCard
import com.jn.echomaze.ui.components.NeonScaffold
import com.jn.echomaze.ui.theme.AppTheme
import com.jn.echomaze.ui.theme.CyberCyan
import com.jn.echomaze.ui.theme.NeonPink
import com.jn.echomaze.ui.theme.NeonYellow

@Composable
fun GameplayScreen(
    levelId: Int,
    coins: Int,
    movesCount: Int,
    maxMoves: Int,
    gridSize: Int,
    tiles: List<Int>,
    imageRes: Int?,
    showNumbersHint: Boolean,
    showPreviewHint: Boolean,
    onTileClick: (Int) -> Unit,
    onPauseClick: () -> Unit,
    onQuickBuyCoins: () -> Unit,
    onHintNumbersClick: () -> Unit,
    onHintPreviewClick: () -> Unit
) {
    NeonScaffold(
        topBar = {
            HeaderBar(
                variant = HeaderVariant.GAMEPLAY,
                title = if (levelId == 0) "DAILY" else levelId.toString().padStart(2, '0'),
                coinBalance = coins,
                onCoinsClick = onQuickBuyCoins,
                onActionClick = onPauseClick,
                actionIcon = Icons.Rounded.Pause
            )
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
                                            gridSize = gridSize,
                                            imageRes = imageRes,
                                            showNumbers = showNumbersHint,
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

            // Hint Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NeonButton(
                    text = if (showNumbersHint) "HIDE #" else "SHOW # (10)",
                    onClick = onHintNumbersClick,
                    modifier = Modifier.weight(1f),
                    color = NeonYellow,
                    icon = Icons.Rounded.Numbers
                )
                NeonButton(
                    text = "PREVIEW",
                    onClick = onHintPreviewClick,
                    modifier = Modifier.weight(1f),
                    color = NeonPink,
                    icon = Icons.Rounded.Visibility
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

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

        if (showPreviewHint && imageRes != null) {
            PreviewHintDialog(imageRes = imageRes, onDismiss = onHintPreviewClick)
        }
    }
}

@Composable
fun PuzzleTile(
    tileId: Int,
    gridSize: Int,
    imageRes: Int?,
    showNumbers: Boolean,
    onClick: () -> Unit
) {
    if (tileId == 0) {
        Box(modifier = Modifier.fillMaxSize())
    } else {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .clickable { onClick() },
            color = if (imageRes != null) Color.Black else CyberCyan.copy(alpha = 0.8f),
            border = BorderStroke(
                2.dp,
                if (imageRes != null) CyberCyan.copy(alpha = 0.5f) else CyberCyan
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (imageRes != null) {
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxSize()
                            .clipToBounds()
                    ) {
                        val originalRow = (tileId - 1) / gridSize
                        val originalCol = (tileId - 1) % gridSize

                        val offsetX = -originalCol * maxWidth.value
                        val offsetY = -originalRow * maxHeight.value

                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(maxWidth * gridSize, maxHeight * gridSize)
                                .offset {
                                    IntOffset(
                                        (offsetX * density).toInt(),
                                        (offsetY * density).toInt()
                                    )
                                },
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }

                if (imageRes == null || showNumbers) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                if (imageRes != null) Color.Black.copy(alpha = 0.4f)
                                else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tileId.toString(),
                            color = if (imageRes != null) Color.White else Color.Black,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PreviewHintDialog(imageRes: Int, onDismiss: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onDismiss() },
        color = Color.Black.copy(alpha = 0.9f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "TARGET IMAGE",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Preview",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, CyberCyan, RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    text = "Tap anywhere to return",
                    color = Color.White.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 24.dp)
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
            imageRes = null,
            showNumbersHint = true,
            showPreviewHint = false,
            onTileClick = {},
            onPauseClick = {},
            onQuickBuyCoins = {},
            onHintNumbersClick = {},
            onHintPreviewClick = {}
        )
    }
}
