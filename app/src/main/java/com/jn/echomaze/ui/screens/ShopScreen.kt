package com.jn.echomaze.ui.screens

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jn.echomaze.billing.CoinProduct
import com.jn.echomaze.ui.components.HeaderBar
import com.jn.echomaze.ui.components.HeaderVariant
import com.jn.echomaze.ui.components.NeonCard
import com.jn.echomaze.ui.components.NeonScaffold
import com.jn.echomaze.ui.theme.AppTheme
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
                onBackClick = onBackClick
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            if (!state.isStoreAvailable) {
                Text(
                    text = "Store currently unavailable",
                    color = NeonPink,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            state.products.forEach { product ->
                CoinProductItem(product = product) { activity ->
                    onEvent(
                        ShopEvent.PurchaseCoinPack(
                            activity,
                            product
                        )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CoinProductItem(product: CoinProduct, onBuy: (Activity) -> Unit) {
    val context = LocalContext.current
    NeonCard(
        color = GoldCoin,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { (context as? Activity)?.let { onBuy(it) } }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = product.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = GoldCoin.copy(alpha = 0.2f),
                border = BorderStroke(1.dp, GoldCoin)
            ) {
                Text(
                    text = product.price,
                    color = GoldCoin,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
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
                    CoinProduct("id1", "1000 Coins", "Small pack of coins", "$0.99", 1000),
                    CoinProduct("id2", "5000 Coins", "Popular pack of coins", "120.000,00 ₫", 5000)
                )
            ),
            onEvent = {},
            onBackClick = {}
        )
    }
}
