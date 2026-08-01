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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.ui.components.HeaderBar
import com.jn.echomaze.ui.components.HeaderVariant
import com.jn.echomaze.ui.components.NeonButton
import com.jn.echomaze.ui.components.NeonScaffold
import com.jn.echomaze.ui.components.glow
import com.jn.echomaze.ui.theme.AppTheme
import com.jn.echomaze.ui.theme.GameTheme
import com.jn.echomaze.ui.theme.NeonPink
import com.jn.echomaze.ui.theme.NeonYellow
import com.jn.echomaze.ui.viewmodel.gameplay.GameplayEvent
import com.jn.echomaze.ui.viewmodel.gameplay.GameplayUiState
import kotlin.random.Random

@Composable
fun GameplayScreen(
    state: GameplayUiState,
    onEvent: (GameplayEvent) -> Unit,
    onPauseClick: () -> Unit,
    onQuickBuyCoins: () -> Unit,
    theme: GameTheme = GameTheme.NeonBlueTheme
) {
    val puzzle = state.puzzle
    val maxMoves = state.gridSize * state.gridSize * 15

    NeonScaffold(
        topBar = {
            HeaderBar(
                variant = HeaderVariant.GAMEPLAY,
                title = "PUZZLE",
                coinBalance = state.coinBalance,
                onCoinsClick = onQuickBuyCoins,
                onActionClick = {
                    onEvent(GameplayEvent.TogglePause)
                    onPauseClick()
                },
                actionIcon = Icons.Rounded.Pause
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Stats Section
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatItem(
                                label = "MOVES",
                                value = "${puzzle?.moves ?: 0} / $maxMoves",
                                color = if ((puzzle?.moves
                                        ?: 0) > maxMoves * 0.8f
                                ) NeonPink else theme.primaryColor
                            )
                            VerticalDivider(
                                modifier = Modifier.height(32.dp),
                                color = Color.White.copy(alpha = 0.1f)
                            )
                            StatItem(
                                label = "TIME",
                                value = formatTimeInSec(state.timeElapsedSeconds),
                                color = theme.primaryColor
                            )
                            VerticalDivider(
                                modifier = Modifier.height(32.dp),
                                color = Color.White.copy(alpha = 0.1f)
                            )
                            StatItem(label = "TARGET", value = "SOLVE IMAGE", color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val progress =
                            ((puzzle?.moves ?: 0).toFloat() / maxMoves.toFloat()).coerceIn(0f, 1f)
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (progress > 0.8f) NeonPink else theme.primaryColor,
                            trackColor = Color.White.copy(alpha = 0.1f),
                            strokeCap = StrokeCap.Round
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hint Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    NeonButton(
                        text = if (state.showNumbersHint) "HIDE #" else "SHOW # (10)",
                        onClick = { onEvent(GameplayEvent.ToggleNumbersHint) },
                        modifier = Modifier.weight(1f),
                        color = NeonYellow,
                        icon = Icons.Rounded.Numbers
                    )
                    NeonButton(
                        text = "PREVIEW",
                        onClick = { onEvent(GameplayEvent.TogglePreviewHint) },
                        modifier = Modifier.weight(1f),
                        color = NeonPink,
                        icon = Icons.Rounded.Visibility
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                Spacer(modifier = Modifier.weight(1f))

                // Puzzle Grid
                BoxWithConstraints(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .aspectRatio(1f)
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    val boardSize = maxWidth
                    val tileSize = boardSize / state.gridSize

                    Column {
                        for (r in 0 until state.gridSize) {
                            Row {
                                for (c in 0 until state.gridSize) {
                                    val index = r * state.gridSize + c
                                    Box(modifier = Modifier.size(tileSize)) {
                                        puzzle?.tiles?.getOrNull(index)?.let { tileId ->
                                            PuzzleTile(
                                                tileId = tileId,
                                                gridSize = state.gridSize,
                                                imageRes = puzzle.imageRes,
                                                showNumber = state.showNumbersHint,
                                                theme = theme,
                                                onClick = { onEvent(GameplayEvent.OnTileClick(index)) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (state.showPreviewHint && puzzle?.imageRes != null) {
                PreviewHintDialog(
                    imageRes = puzzle.imageRes,
                    theme = theme,
                    onDismiss = { onEvent(GameplayEvent.TogglePreviewHint) })
            }

            if (state.isSolved) {
                VictoryDialog(
                    score = state.earnedScore,
                    coins = state.earnedCoins,
                    onNextClick = { onEvent(GameplayEvent.StartPuzzle(Random.nextInt())) },
                    theme = theme
                )
            }
        }
    }
}

@Composable
fun VictoryDialog(score: Int, coins: Int, onNextClick: () -> Unit, theme: GameTheme) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.8f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "VICTORY!",
                style = MaterialTheme.typography.displayMedium,
                color = theme.primaryColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Score: $score",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
            Text(
                text = "+$coins COINS earned",
                style = MaterialTheme.typography.titleMedium,
                color = NeonYellow
            )
            Spacer(modifier = Modifier.height(48.dp))
            NeonButton(
                text = "NEXT PUZZLE",
                onClick = onNextClick,
                color = theme.primaryColor
            )
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = color,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatTimeInSec(seconds: Long): String {
    val m = seconds / 60
    val s = seconds % 60
    return "${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}"
}

@Composable
fun PuzzleTile(
    tileId: Int,
    gridSize: Int,
    imageRes: Int?,
    showNumber: Boolean,
    theme: GameTheme = GameTheme.NeonBlueTheme,
    onClick: () -> Unit
) {
    if (tileId == 0) {
        Box(modifier = Modifier.fillMaxSize())
    } else {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() },
            color = if (imageRes != null) theme.tileColor else theme.primaryColor.copy(alpha = 0.8f),
            shape = RectangleShape
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (imageRes != null) {
                    val painter = painterResource(id = imageRes)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clipToBounds()
                            .drawWithContent {
                                val tileSize = size.width
                                val boardSize = tileSize * gridSize
                                val originalRow = (tileId - 1) / gridSize
                                val originalCol = (tileId - 1) % gridSize

                                drawIntoCanvas { canvas ->
                                    canvas.withSave {
                                        canvas.translate(
                                            -originalCol * tileSize,
                                            -originalRow * tileSize
                                        )
                                        with(painter) { draw(size = Size(boardSize, boardSize)) }
                                    }
                                }
                            }
                    )
                }

                if (imageRes == null || showNumber) {
                    Text(
                        text = tileId.toString(),
                        color = if (imageRes != null) theme.textColor else Color.Black,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        modifier = Modifier.glow(if (imageRes != null) theme.tileColor else Color.Transparent)
                    )
                }
            }
        }
    }
}

@Composable
fun PreviewHintDialog(
    imageRes: Int,
    theme: GameTheme = GameTheme.NeonBlueTheme,
    onDismiss: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onDismiss() },
        color = Color.Black.copy(alpha = 0.9f)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(32.dp), contentAlignment = Alignment.Center) {
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
                        .border(2.dp, theme.primaryColor, RoundedCornerShape(16.dp)),
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
            state = GameplayUiState(
                timeElapsedSeconds = 45,
                gridSize = 3,
                puzzle = com.jn.echomaze.domain.model.Puzzle(
                    tiles = listOf(1, 2, 3, 4, 5, 6, 7, 8, 0),
                    gridSize = 3,
                    imageRes = com.jn.echomaze.R.drawable.assets_1
                )
            ),
            onEvent = {},
            onPauseClick = {},
            onQuickBuyCoins = {}
        )
    }
}
