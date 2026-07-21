package com.jn.echomaze.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Leaderboard
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.ui.components.IconButtonGlow
import com.jn.echomaze.ui.components.glow
import com.jn.echomaze.ui.theme.CyberCyan
import com.jn.echomaze.ui.theme.EchoMazeTheme
import com.jn.echomaze.ui.theme.GoldCoin
import com.jn.echomaze.ui.theme.NeonPurple
import com.jn.echomaze.ui.theme.PressStart2P

@Composable
fun MainMenuScreen(
    coinBalance: Int,
    onPlayClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onShopClick: () -> Unit,
    onLeaderboardClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Background Decorative Maze Lines (Simplified)
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = CyberCyan.copy(alpha = 0.05f),
                radius = 400.dp.toPx(),
                style = Stroke(width = 2.dp.toPx())
            )
            drawCircle(
                color = NeonPurple.copy(alpha = 0.05f),
                radius = 300.dp.toPx(),
                style = Stroke(width = 2.dp.toPx())
            )
        }

        Box(modifier = Modifier.fillMaxSize().systemBarsPadding().padding(24.dp)) {
            // Coin Balance Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        .border(2.dp, GoldCoin.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = coinBalance.toString(),
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = PressStart2P
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Rounded.MonetizationOn,
                        contentDescription = null,
                        tint = GoldCoin,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                // Title
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "EchoMaze:",
                        color = Color.White,
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp,
                            fontFamily = PressStart2P
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Pulse Runner",
                        color = CyberCyan,
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 4.sp,
                            fontFamily = PressStart2P
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.glow(CyberCyan, alpha = 0.3f, blurRadius = 12.dp)
                    )
                }

                // Central Icon Placeholder (Orb/Maze)
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .glow(NeonPurple, alpha = 0.4f, blurRadius = 40.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(NeonPurple.copy(alpha = 0.4f), Color.Transparent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Simplified Maze Orb
                    Canvas(modifier = Modifier.size(140.dp)) {
                        drawCircle(
                            brush = Brush.sweepGradient(listOf(CyberCyan, NeonPurple, CyberCyan)),
                            style = Stroke(width = 4.dp.toPx())
                        )
                        drawCircle(
                            color = Color.White.copy(alpha = 0.8f),
                            radius = 10.dp.toPx()
                        )
                    }
                }

                // Bottom Buttons
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MenuSmallButton(Icons.Rounded.Settings, "Settings", onSettingsClick)
                        MenuSmallButton(Icons.Rounded.ShoppingCart, "Shop", onShopClick)
                        MenuSmallButton(Icons.Rounded.Leaderboard, "Stats", onLeaderboardClick)
                    }

                    // Play Button
                    PlayButton(onClick = onPlayClick)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun MenuSmallButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        IconButtonGlow(icon = icon, contentDescription = label, onClick = onClick, color = Color.White.copy(alpha = 0.7f))
        Text(
            text = label.uppercase(), 
            color = Color.White.copy(alpha = 0.7f), 
            style = MaterialTheme.typography.labelSmall.copy(fontFamily = PressStart2P)
        )
    }
}

@Composable
fun PlayButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(80.dp)
            .glow(CyberCyan, alpha = 0.6f, borderRadius = 8.dp, blurRadius = 24.dp, offsetY = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(CyberCyan.copy(alpha = 0.8f), NeonPurple.copy(alpha = 0.8f))
                )
            )
            .border(4.dp, Color.White, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = "Play",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "START",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = PressStart2P,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 4.sp
                )
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun MainMenuPreview() {
    EchoMazeTheme {
        MainMenuScreen(120, {}, {}, {}, {})
    }
}
