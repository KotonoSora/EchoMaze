package com.jn.echomaze.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.ui.theme.CyberCyan
import com.jn.echomaze.ui.theme.NeonPurple

@Composable
fun NeonScaffold(
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        containerColor = Color.Transparent, // Let the background show through
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing // Scaffold handles safe areas
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .drawBehind {
                    val gridSize = 40.dp.toPx()
                    val gridColor = Color(0xFF1A1A3A)

                    // Vertical lines
                    var x = 0f
                    while (x < size.width) {
                        drawLine(
                            color = gridColor,
                            start = Offset(x, 0f),
                            end = Offset(x, size.height),
                            strokeWidth = 1f
                        )
                        x += gridSize
                    }

                    // Horizontal lines
                    var y = 0f
                    while (y < size.height) {
                        drawLine(
                            color = gridColor,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1f
                        )
                        y += gridSize
                    }
                }
                .padding(innerPadding)
        ) {
            content(innerPadding)
        }
    }
}

@Composable
fun NeonButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = CyberCyan,
    cornerRadius: Dp = 8.dp,
    glowRadius: Dp = 16.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .glow(
                color = color,
                borderRadius = cornerRadius,
                blurRadius = glowRadius,
                offsetY = 2.dp
            )
            .clip(RoundedCornerShape(cornerRadius))
            .clickable(onClick = onClick),
        color = Color.Black.copy(alpha = 0.7f),
        border = BorderStroke(2.dp, color)
    ) {
        Row(
            modifier = Modifier
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            content = content
        )
    }
}

@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = CyberCyan,
    icon: ImageVector? = null
) {
    NeonButton(
        onClick = onClick,
        modifier = modifier,
        color = color
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(
            text = text.uppercase(),
            color = color,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        )
    }
}

@Composable
fun NeonCard(
    modifier: Modifier = Modifier,
    color: Color = NeonPurple,
    cornerRadius: Dp = 12.dp,
    glowRadius: Dp = 16.dp,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .glow(
                color = color,
                borderRadius = cornerRadius,
                blurRadius = glowRadius,
                alpha = 0.2f
            ),
        shape = RoundedCornerShape(cornerRadius),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        border = BorderStroke(2.dp, color.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}

@Composable
fun NeonLoadingSpinner(
    modifier: Modifier = Modifier,
    color: Color = CyberCyan
) {
    Box(
        modifier = modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = color,
            strokeWidth = 4.dp,
            modifier = Modifier.glow(color, alpha = 0.5f, blurRadius = 12.dp)
        )
    }
}

@Composable
fun NeonTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    fontSize: androidx.compose.ui.unit.TextUnit = 24.sp
) {
    NeonText(
        text = text.uppercase(),
        modifier = modifier,
        style = MaterialTheme.typography.displaySmall.copy(
            color = color,
            fontSize = fontSize,
            letterSpacing = 2.sp
        ),
        color = color,
        glowRadius = 8.dp
    )
}

@Composable
fun NeonText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.White,
    glowRadius: Dp = 4.dp
) {
    Text(
        text = text,
        modifier = modifier.glow(color, alpha = 0.8f, blurRadius = glowRadius),
        style = style.copy(color = color)
    )
}
