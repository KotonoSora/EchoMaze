package com.jn.echomaze.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jn.echomaze.ui.components.GlowButton
import com.jn.echomaze.ui.components.IconButtonGlow
import com.jn.echomaze.ui.components.glow
import com.jn.echomaze.ui.theme.*

data class ShopItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val icon: ImageVector,
    val color: Color,
    val isIAP: Boolean = false
)

@Composable
fun ShopScreen(
    coinBalance: Int,
    onBackClick: () -> Unit,
    onBuyUpgrade: (ShopItem) -> Unit,
    onBuyCoins: (String) -> Unit,
    onRestorePurchases: () -> Unit
) {
    val upgrades = listOf(
        ShopItem("radius", "Pulse Radius", "Increase pulse reveal area.", 500, Icons.Rounded.RadioButtonChecked, CyberCyan),
        ShopItem("duration", "Pulse Duration", "Make pulses last longer.", 300, Icons.Rounded.History, NeonPurple),
        ShopItem("hint", "Hint Path", "Temporarily show the correct path.", 1000, Icons.Rounded.Route, GoldCoin)
    )

    val coinPacks = listOf(
        ShopItem("coins_100", "100 Coins", "Starter pack", 29, Icons.Rounded.Add, GoldCoin, true),
        ShopItem("coins_500", "500 Coins", "Explorer pack", 49, Icons.Rounded.AddCircle, CyberCyan, true),
        ShopItem("coins_1000", "1000 Coins", "Pro pack", 69, Icons.Rounded.MonetizationOn, NeonPurple, true),
        ShopItem("coins_1500", "1500 Coins", "Pro+ pack", 99, Icons.Rounded.MonetizationOn, NeonPurple, true),
        ShopItem("coins_2000", "2000 Coins", "Expert pack", 199, Icons.Rounded.Stars, ElectricBlue, true),
        ShopItem("coins_2500", "2500 Coins", "Master pack", 399, Icons.Rounded.Stars, ElectricBlue, true),
        ShopItem("coins_3000", "3000 Coins", "Master+ pack", 499, Icons.Rounded.Stars, ElectricBlue, true),
        ShopItem("coins_3500", "3500 Coins", "Legend pack", 799, Icons.Rounded.Diamond, GoldCoin, true),
        ShopItem("coins_4000", "4000 Coins", "Ultimate pack", 999, Icons.Rounded.Diamond, GoldCoin, true)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .padding(24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButtonGlow(
                icon = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                onClick = onBackClick
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = coinBalance.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
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

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SHOP",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold),
                color = Color.White
            )
            TextButton(onClick = onRestorePurchases) {
                Text("Restore", color = CyberCyan)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "UPGRADES", style = MaterialTheme.typography.titleMedium, color = CyberCyan)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(upgrades) { item ->
                ShopCard(item = item, onBuy = { onBuyUpgrade(item) })
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "GET COINS", style = MaterialTheme.typography.titleMedium, color = NeonPurple)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(coinPacks) { item ->
                ShopCard(item = item, onBuy = { onBuyCoins(item.id) })
            }
        }
    }
}

@Composable
fun ShopCard(item: ShopItem, onBuy: () -> Unit) {
    Surface(
        modifier = Modifier
            .width(180.dp)
            .height(260.dp)
            .glow(item.color, alpha = 0.15f, borderRadius = 24.dp),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        border = androidx.compose.foundation.BorderStroke(1.dp, item.color.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = item.color,
                modifier = Modifier.size(48.dp)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = item.name, style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

            GlowButton(
                text = if (item.isIAP) "$${item.price / 100.0}" else "${item.price}",
                onClick = onBuy,
                color = item.color,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun ShopPreview() {
    EchoMazeTheme {
        ShopScreen(1250, {}, {}, {}, {})
    }
}
