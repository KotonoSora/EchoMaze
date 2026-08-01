package com.jn.echomaze.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.domain.model.UserStats
import com.jn.echomaze.ui.theme.AppTheme
import com.jn.echomaze.ui.theme.NeonBlue
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
        if (variant != HeaderVariant.HOME) {
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
            NeonText(
                text = title,
                style = if (variant == HeaderVariant.GAMEPLAY) {
                    MaterialTheme.typography.titleLarge.copy(
                        fontSize = 16.sp,
                        fontFamily = PressStart2P
                    )
                } else {
                    MaterialTheme.typography.titleLarge
                },
                color = if (variant == HeaderVariant.GAMEPLAY) Color.White.ensureContrast() else NeonBlue.ensureContrast(),
                glowRadius = 4.dp
            )
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

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun HeaderBarHomePreview() {
    AppTheme {
        HeaderBar(
            variant = HeaderVariant.HOME,
            coinBalance = 1250,
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
            title = "PUZZLE",
            coinBalance = 1250,
            onActionClick = {},
            actionIcon = Icons.Default.ShoppingCart
        )
    }
}
