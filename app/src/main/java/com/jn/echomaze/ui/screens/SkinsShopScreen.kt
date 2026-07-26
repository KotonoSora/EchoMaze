package com.jn.echomaze.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.ui.components.HeaderBar
import com.jn.echomaze.ui.components.HeaderVariant
import com.jn.echomaze.ui.components.NeonButton
import com.jn.echomaze.ui.components.NeonCard
import com.jn.echomaze.ui.components.NeonScaffold
import com.jn.echomaze.ui.theme.AppTheme
import com.jn.echomaze.ui.theme.CyberCyan
import com.jn.echomaze.ui.theme.GoldCoin
import com.jn.echomaze.ui.theme.NeonPurple

@Composable
fun SkinsShopScreen(
    coinBalance: Int,
    onBackClick: () -> Unit,
    onBuyUpgrade: (String) -> Unit
) {
    NeonScaffold(
        topBar = {
            HeaderBar(
                variant = HeaderVariant.STANDARD,
                title = "BOUTIQUE",
                coinBalance = coinBalance,
                onBackClick = onBackClick
            )
        }
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // NeonTitle is now in HeaderBar
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "CUSTOMIZE YOUR EXPERIENCE",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            SkinsSection(
                "POWER-UPS", listOf(
                    SkinItem(
                        "hint",
                        "SHOW GOAL",
                        "Displays the target puzzle state.",
                        300,
                        Icons.Rounded.Lightbulb,
                        GoldCoin
                    ),
                    SkinItem(
                        "undo",
                        "REVERSE",
                        "Undo your last tile move.",
                        150,
                        Icons.AutoMirrored.Rounded.Undo,
                        CyberCyan
                    )
                ), onBuyUpgrade
            )

            Spacer(modifier = Modifier.height(40.dp))

            SkinsSection(
                "THEMES", listOf(
                    SkinItem(
                        "skin_neon",
                        "NEON BLUE",
                        "The classic grid look.",
                        500,
                        Icons.Rounded.Palette,
                        CyberCyan
                    ),
                    SkinItem(
                        "skin_sunset",
                        "SUNSET",
                        "Warm glow for your tiles.",
                        500,
                        Icons.Rounded.Palette,
                        NeonPurple
                    ),
                    SkinItem(
                        "skin_emerald",
                        "EMERALD",
                        "Lush green aesthetic.",
                        750,
                        Icons.Rounded.Palette,
                        Color.Green
                    )
                ), onBuyUpgrade
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

data class SkinItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun SkinsSection(
    title: String,
    items: List<SkinItem>,
    onBuy: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleSmall, color = Color.White)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { item ->
                SkinCard(item = item, onBuy = { onBuy(item.id) })
            }
        }
    }
}

@Composable
fun SkinCard(item: SkinItem, onBuy: () -> Unit) {
    NeonCard(
        modifier = Modifier
            .width(180.dp)
            .height(220.dp),
        color = item.color
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(item.icon, null, tint = item.color, modifier = Modifier.size(40.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    item.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    item.description,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp,
                    fontSize = 7.sp
                )
            }

            NeonButton(
                text = "${item.price}",
                onClick = onBuy,
                color = item.color,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SkinsShopScreenPreview() {
    AppTheme {
        SkinsShopScreen(
            coinBalance = 1200,
            onBackClick = {},
            onBuyUpgrade = {}
        )
    }
}
