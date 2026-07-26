package com.jn.echomaze.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Diamond
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.echomaze.billing.CoinProduct
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
fun ShopScreen(
    coinBalance: Int,
    isStoreAvailable: Boolean,
    coinProducts: List<CoinProduct>,
    adCooldown: Long,
    onBackClick: () -> Unit,
    onWatchAd: () -> Unit,
    onBuyProduct: (CoinProduct) -> Unit,
    onBuyUpgrade: (String) -> Unit,
    onRestorePurchases: () -> Unit
) {
    NeonScaffold(
        topBar = {
            HeaderBar(
                variant = HeaderVariant.STANDARD,
                title = "TREASURY",
                coinBalance = coinBalance,
                onBackClick = onBackClick
            )
        }
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // HeaderBar already has title, but this screen has it inside Column too.
                // I'll keep the text inside but maybe remove NeonTitle from HeaderBar if it feels redundant?
                // The requirement says "other screen always have header bar with back icon, title on left, number of coins on right"
                // So I'll remove the redundant NeonTitle from the Column.
                Spacer(modifier = Modifier.height(0.dp))
                TextButton(onClick = onRestorePurchases) {
                    Text("RESTORE", color = CyberCyan, style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (!isStoreAvailable && coinProducts.isEmpty()) {
                StoreUnavailableState(onRetry = onRestorePurchases)
            } else if (coinProducts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "NO ITEMS FOR SALE", color = Color.Gray)
                }
            } else {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    CoinGridSection(
                        coinProducts = coinProducts,
                        adCooldown = adCooldown,
                        onWatchAd = onWatchAd,
                        onBuyProduct = onBuyProduct
                    )

                    UpgradeSection(onBuyUpgrade = onBuyUpgrade)

                    NeonButton(
                        text = "SKINS & POWER-UPS",
                        onClick = { /* Navigate to skins */ },
                        modifier = Modifier.fillMaxWidth(),
                        color = NeonPurple,
                        icon = Icons.Rounded.Palette
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun StoreUnavailableState(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.CloudOff,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "STORE CURRENTLY UNAVAILABLE",
            color = Color.Gray,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(32.dp))
        NeonButton(text = "TRY AGAIN", onClick = onRetry, color = CyberCyan)
    }
}

@Composable
fun CoinGridSection(
    coinProducts: List<CoinProduct>,
    adCooldown: Long,
    onWatchAd: () -> Unit,
    onBuyProduct: (CoinProduct) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "COIN PACKS", style = MaterialTheme.typography.titleSmall, color = CyberCyan)

        val itemsPerRow = 2
        val totalItems = coinProducts.size + 1
        val rows = (totalItems + itemsPerRow - 1) / itemsPerRow

        for (i in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (j in 0 until itemsPerRow) {
                    val index = i * itemsPerRow + j
                    if (index == 0) {
                        Box(modifier = Modifier.weight(1f)) {
                            AdRewardCard(adCooldown, onWatchAd)
                        }
                    } else if (index - 1 < coinProducts.size) {
                        Box(modifier = Modifier.weight(1f)) {
                            CoinCard(coinProducts[index - 1], onBuyProduct)
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AdRewardCard(cooldown: Long, onWatchAd: () -> Unit) {
    val isAvailable = cooldown == 0L
    NeonCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        color = if (isAvailable) GoldCoin else Color.Gray
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Rounded.Tv,
                contentDescription = null,
                tint = if (isAvailable) GoldCoin else Color.Gray,
                modifier = Modifier.size(40.dp)
            )

            Text(
                text = "WATCH AD",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = if (isAvailable) "REWARD: 50 COINS" else "WAIT: ${formatTime(cooldown)}",
                style = MaterialTheme.typography.labelSmall,
                color = if (isAvailable) CyberCyan else Color.Gray,
                fontSize = 8.sp,
                textAlign = TextAlign.Center
            )

            NeonButton(
                text = if (isAvailable) "WATCH" else "LOCKED",
                onClick = onWatchAd,
                modifier = Modifier.fillMaxWidth(),
                color = if (isAvailable) GoldCoin else Color.Gray
            )
        }
    }
}

@Composable
fun CoinCard(product: CoinProduct, onBuy: (CoinProduct) -> Unit) {
    NeonCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        color = CyberCyan
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = getCoinIcon(product.coins),
                contentDescription = null,
                tint = GoldCoin,
                modifier = Modifier.size(40.dp)
            )

            Text(
                text = product.name,
                style = MaterialTheme.typography.titleSmall,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = "UNLOCK BOOSTS!",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 7.sp,
                textAlign = TextAlign.Center,
                lineHeight = 10.sp
            )

            NeonButton(
                text = product.price,
                onClick = { onBuy(product) },
                modifier = Modifier.fillMaxWidth(),
                color = CyberCyan
            )
        }
    }
}

@Composable
fun UpgradeSection(onBuyUpgrade: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "BOOSTS", style = MaterialTheme.typography.titleSmall, color = GoldCoin)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                UpgradeCard(
                    id = "hint",
                    name = "SHOW GOAL",
                    price = 300,
                    icon = Icons.Rounded.Lightbulb,
                    color = GoldCoin
                ) { onBuyUpgrade("hint") }
            }
            item {
                UpgradeCard(
                    id = "undo",
                    name = "REVERSE MOVE",
                    price = 150,
                    icon = Icons.AutoMirrored.Rounded.Undo,
                    color = CyberCyan
                ) { onBuyUpgrade("undo") }
            }
        }
    }
}

@Composable
fun UpgradeCard(
    id: String,
    name: String,
    price: Int,
    icon: ImageVector,
    color: Color,
    onBuy: () -> Unit
) {
    NeonCard(
        modifier = Modifier
            .width(160.dp)
            .height(180.dp),
        color = color
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(32.dp))
            Text(
                name,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            NeonButton(
                text = "$price",
                onClick = onBuy,
                color = color,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

fun getCoinIcon(amount: Int): ImageVector {
    return when {
        amount <= 500 -> Icons.Rounded.MonetizationOn
        amount <= 1500 -> Icons.Rounded.Savings
        else -> Icons.Rounded.Diamond
    }
}

fun formatTime(seconds: Long): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%02d:%02d".format(m, s)
}

@Preview(showBackground = true)
@Composable
fun ShopScreenPreview() {
    AppTheme {
        ShopScreen(
            coinBalance = 500,
            isStoreAvailable = true,
            coinProducts = listOf(
                CoinProduct("coins_100", "100 Coins", "Description", "$0.29", 100),
                CoinProduct("coins_500", "500 Coins", "Description", "$0.49", 500)
            ),
            adCooldown = 0L,
            onBackClick = {},
            onWatchAd = {},
            onBuyProduct = {},
            onBuyUpgrade = {},
            onRestorePurchases = {}
        )
    }
}
