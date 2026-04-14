package com.kotonosora.echomaze.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotonosora.echomaze.engine.MazeData
import com.kotonosora.echomaze.engine.PulseEngine
import com.kotonosora.echomaze.ui.components.glow
import com.kotonosora.echomaze.ui.theme.CyberCyan
import com.kotonosora.echomaze.ui.theme.GoldCoin
import com.kotonosora.echomaze.ui.theme.NeonPurple
import com.kotonosora.echomaze.ui.theme.PressStart2P
import kotlin.math.max

@Composable
fun GameplayScreen(
    levelId: Int,
    coins: Int,
    pulsesRemaining: Int,
    pulseEngine: PulseEngine,
    mazeData: MazeData?,
    playerPos: Offset,
    onMove: (Offset) -> Unit,
    onPauseClick: () -> Unit,
    onPulseClick: (Float, Float) -> Unit,
    onBuyExtraPulses: () -> Unit,
    onRevealMap: () -> Unit,
    onBuyHint: () -> Unit
) {
    val pulses = pulseEngine.activePulses
    
    // Create a state that updates every frame to drive animations
    val frameTime by produceState(initialValue = System.currentTimeMillis()) {
        while (true) {
            withFrameMillis {
                value = System.currentTimeMillis()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Game Area / Maze
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(playerPos) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        onMove(playerPos + dragAmount)
                    }
                }
        ) {
            val now = frameTime
            val walls = mazeData?.walls ?: emptyList()
            
            // Draw walls based on pulse visibility
            walls.forEach { wall ->
                var maxVisibility = 0f
                pulses.forEach { pulse ->
                    maxVisibility = max(maxVisibility, pulse.getVisibilityAt(wall.bounds, now))
                }
                
                if (maxVisibility > 0f) {
                    drawRect(
                        color = CyberCyan.copy(alpha = maxVisibility),
                        topLeft = wall.bounds.topLeft,
                        size = wall.bounds.size
                    )
                    drawRect(
                        color = Color.White.copy(alpha = maxVisibility * 0.5f),
                        topLeft = wall.bounds.topLeft,
                        size = wall.bounds.size,
                        style = Stroke(width = 1.dp.toPx())
                    )
                }
            }

            // Draw Exit
            mazeData?.let { data ->
                var exitVisibility = 0.2f
                pulses.forEach { pulse ->
                    val dx = pulse.centerX - data.exitPos.x
                    val dy = pulse.centerY - data.exitPos.y
                    val dist = kotlin.math.sqrt(dx * dx + dy * dy)
                    if (dist < pulse.getCurrentRadius(now)) {
                        exitVisibility = 1.0f
                    }
                }

                drawCircle(
                    color = NeonPurple.copy(alpha = exitVisibility),
                    radius = 40f,
                    center = data.exitPos,
                    style = Stroke(width = 4f)
                )
                drawCircle(
                    color = NeonPurple.copy(alpha = exitVisibility * 0.5f),
                    radius = 20f + (now % 1000 / 50f),
                    center = data.exitPos
                )
            }

            // Draw pulses
            pulses.forEach { pulse ->
                val progress = pulse.getProgress(now)
                val radius = pulse.getCurrentRadius(now)
                drawCircle(
                    color = CyberCyan.copy(alpha = (1f - progress) * 0.8f),
                    radius = radius,
                    center = Offset(pulse.centerX, pulse.centerY),
                    style = Stroke(width = 6.dp.toPx())
                )
            }

            // Draw player (Orb)
            drawCircle(
                color = Color.White,
                radius = 12.dp.toPx(),
                center = playerPos
            )
            drawCircle(
                color = CyberCyan,
                radius = 16.dp.toPx(),
                center = playerPos,
                style = Stroke(width = 2.dp.toPx())
            )
        }

        // HUD - Top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "LEVEL",
                    color = Color.White.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = PressStart2P)
                )
                Text(
                    text = levelId.toString().padStart(2, '0'),
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = PressStart2P
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .border(2.dp, GoldCoin.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Pause,
                    contentDescription = "Pause",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onPauseClick() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "$coins",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = PressStart2P
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Rounded.MonetizationOn,
                    contentDescription = null,
                    tint = GoldCoin,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Bottom Controls
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CoinActionButton(Icons.Rounded.Add, "PULSE", "50", onBuyExtraPulses)
                CoinActionButton(Icons.Rounded.Map, "REVEAL", "100", onRevealMap)
                CoinActionButton(Icons.Rounded.Lightbulb, "HINT", "75", onBuyHint)
            }

            PulseButton(
                pulsesRemaining = pulsesRemaining,
                onClick = {
                    if (pulsesRemaining > 0) {
                        onPulseClick(playerPos.x, playerPos.y)
                    }
                }
            )
        }
    }
}

@Composable
fun CoinActionButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, cost: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .glow(GoldCoin, alpha = 0.3f, borderRadius = 8.dp, blurRadius = 8.dp, offsetY = 2.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
                .border(2.dp, GoldCoin.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = GoldCoin, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.8f),
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = PressStart2P,
                fontSize = 8.sp
            )
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = cost,
                color = Color.White,
                style = MaterialTheme.typography.labelSmall.copy(fontFamily = PressStart2P, fontSize = 8.sp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(imageVector = Icons.Rounded.MonetizationOn, contentDescription = null, tint = GoldCoin, modifier = Modifier.size(10.dp))
        }
    }
}

@Composable
fun PulseButton(pulsesRemaining: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(140.dp)
            .glow(CyberCyan, alpha = 0.5f, borderRadius = 16.dp, blurRadius = 24.dp, offsetY = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(CyberCyan.copy(alpha = 0.4f), Color.Transparent)
                )
            )
            .border(4.dp, CyberCyan, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick, enabled = pulsesRemaining > 0),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "PULSE",
                color = if (pulsesRemaining > 0) Color.White else Color.White.copy(alpha = 0.5f),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp,
                    fontFamily = PressStart2P
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$pulsesRemaining LEFT",
                color = if (pulsesRemaining > 0) CyberCyan else Color.Red,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontFamily = PressStart2P,
                    fontSize = 10.sp
                )
            )
        }
    }
}
