package com.jn.echomaze.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.domain.model.UserStats
import com.jn.echomaze.ui.theme.AppTheme
import com.jn.echomaze.ui.theme.CyberCyan
import com.jn.echomaze.ui.theme.GoldCoin
import com.jn.echomaze.ui.theme.NeonBlue
import com.jn.echomaze.ui.theme.NeonPurple
import com.jn.echomaze.ui.theme.NeonYellow
import com.jn.echomaze.ui.theme.PressStart2P
import com.jn.echomaze.ui.theme.ensureContrast

enum class HeaderVariant {
    HOME, STANDARD, GAMEPLAY
}

@Composable
fun HeaderBar(
    variant: HeaderVariant,
    coinBalance: Int,
    modifier: Modifier = Modifier,
    stats: UserStats? = null,
    title: String? = null,
    onBackClick: (() -> Unit)? = null,
    onShopClick: (() -> Unit)? = null,
    onActionClick: (() -> Unit)? = null,
    actionIcon: ImageVector? = null,
    onCoinsClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Section: Back button or Profile
        if (variant == HeaderVariant.HOME) {
            PlayerProfileBar(stats)
        } else {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = NeonBlue
                    )
                }
            } else {
                Spacer(Modifier.width(8.dp))
            }
        }

        // Title
        if (title != null) {
            if (variant == HeaderVariant.GAMEPLAY) {
                Column {
                    Text(
                        text = if (title == "DAILY") "DAILY" else "LEVEL",
                        color = NeonBlue.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 8.sp,
                            letterSpacing = 1.sp,
                            fontFamily = PressStart2P
                        )
                    )
                    NeonText(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 16.sp,
                            fontFamily = PressStart2P
                        ),
                        color = Color.White.ensureContrast(),
                        glowRadius = 4.dp
                    )
                }
            } else {
                NeonText(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = NeonBlue.ensureContrast(),
                    glowRadius = 4.dp
                )
            }
        }

        Spacer(Modifier.weight(1f))

        // Right Section: Coins and Shop/Action
        NeonCard(
            color = NeonYellow,
            glowRadius = 2.dp,
            cornerRadius = 12.dp,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clickable(enabled = onCoinsClick != null) { onCoinsClick?.invoke() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = "Coins",
                    tint = NeonYellow,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = coinBalance.toString(),
                    color = NeonYellow.ensureContrast(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (variant == HeaderVariant.HOME && onShopClick != null) {
            Spacer(Modifier.width(12.dp))
            NeonButton(
                onClick = onShopClick,
                color = NeonBlue,
                cornerRadius = 12.dp,
                glowRadius = 2.dp,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Shop",
                    tint = NeonBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
        } else if (onActionClick != null && actionIcon != null) {
            Spacer(Modifier.width(12.dp))
            NeonButton(
                onClick = onActionClick,
                color = if (variant == HeaderVariant.GAMEPLAY) NeonBlue else Color.White,
                cornerRadius = 12.dp,
                glowRadius = 2.dp,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = "Action",
                    tint = if (variant == HeaderVariant.GAMEPLAY) NeonBlue else Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
        } else {
            Spacer(Modifier.width(16.dp))
        }
    }
}

@Composable
fun PlayerProfileBar(stats: UserStats?) {
    val xp = stats?.totalXp ?: 0
    val level = (xp / 1000) + 1
    val xpInLevel = xp % 1000
    val progress = xpInLevel / 1000f

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(NeonPurple)
                .border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                .glow(NeonPurple, alpha = 0.4f, borderRadius = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "L$level",
                color = NeonPurple.ensureContrast(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = PressStart2P,
                    fontSize = 9.sp
                )
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        if ((stats?.loginStreak ?: 0) > 1) {
            NeonCard(
                color = GoldCoin,
                cornerRadius = 12.dp,
                glowRadius = 2.dp,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "🔥", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${stats?.loginStreak}",
                        color = GoldCoin.ensureContrast(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = PressStart2P,
                            fontSize = 10.sp
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
        }

        Column {
            Text(
                text = "LEVEL UP",
                color = Color.White,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 6.sp,
                    fontFamily = PressStart2P,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .width(60.dp)
                    .height(6.dp)
                    .clip(CircleShape)
                    .border(1.dp, CyberCyan.copy(alpha = 0.3f), CircleShape),
                color = CyberCyan,
                trackColor = Color.White.copy(alpha = 0.15f),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun HeaderBarHomePreview() {
    AppTheme {
        HeaderBar(
            variant = HeaderVariant.HOME,
            coinBalance = 1250,
            stats = UserStats(
                totalXp = 2500,
                loginStreak = 5
            ),
            onShopClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun HeaderBarStandardPreview() {
    AppTheme {
        HeaderBar(
            variant = HeaderVariant.STANDARD,
            title = "TREASURY",
            coinBalance = 1250,
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun HeaderBarGameplayPreview() {
    AppTheme {
        HeaderBar(
            variant = HeaderVariant.GAMEPLAY,
            title = "05",
            coinBalance = 1250,
            onActionClick = {},
            actionIcon = Icons.Rounded.Add
        )
    }
}
