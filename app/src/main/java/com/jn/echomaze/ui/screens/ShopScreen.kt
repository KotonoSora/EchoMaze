package com.jn.echomaze.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
import com.jn.echomaze.ui.theme.NeonPink
import com.jn.echomaze.ui.viewmodel.shop.ShopEvent
import com.jn.echomaze.ui.viewmodel.shop.ShopUiState

@Composable
fun ShopScreen(
    state: ShopUiState,
    onEvent: (ShopEvent) -> Unit,
    onBackClick: () -> Unit
) {
    NeonScaffold(
        topBar = {
            HeaderBar(
                variant = HeaderVariant.STANDARD,
                title = "SHOP",
                coinBalance = state.coinBalance,
                onActionClick = onBackClick,
                actionIcon = Icons.Rounded.ArrowBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            SectionTitle("COIN PACKS")

            if (!state.isStoreAvailable) {
                Text(
                    text = "Store currently unavailable",
                    color = NeonPink,
                    modifier = Modifier.padding(16.dp)
                )
            }

            state.products.forEach { product ->
                CoinProductItem(product = product) {
                    onEvent(
                        ShopEvent.PurchaseCoinPack(
                            it,
                            product
                        )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            NeonButton(
                text = "RESTORE PURCHASES",
                onClick = { onEvent(ShopEvent.RestorePurchases) },
                color = Color.Gray,
                icon = Icons.Rounded.Restore,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier
            .weight(1f)
            .height(1.dp)
            .background(Color.White.copy(alpha = 0.1f)))
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.White.copy(alpha = 0.5f),
            style = MaterialTheme.typography.labelMedium,
            letterSpacing = 2.sp
        )
        Box(modifier = Modifier
            .weight(1f)
            .height(1.dp)
            .background(Color.White.copy(alpha = 0.1f)))
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun CoinProductItem(product: CoinProduct, onBuy: (android.app.Activity) -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    NeonCard(color = GoldCoin) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { onBuy(context as android.app.Activity) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Rounded.ShoppingBag,
                    contentDescription = null,
                    tint = GoldCoin,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = product.name, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(
                        text = product.description,
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Text(
                text = product.price,
                color = GoldCoin,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShopPreview() {
    AppTheme {
        ShopScreen(
            state = ShopUiState(
                coinBalance = 500,
                isStoreAvailable = true,
                products = listOf(
                    CoinProduct("id1", "1000 Coins", "Small pack of coins", "$0.99", 1000)
                )
            ),
            onEvent = {},
            onBackClick = {}
        )
    }
}
