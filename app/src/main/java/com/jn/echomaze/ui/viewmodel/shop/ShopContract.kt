package com.jn.echomaze.ui.viewmodel.shop

import android.app.Activity
import com.jn.echomaze.billing.CoinProduct
import com.jn.echomaze.domain.model.UserStats

data class ShopUiState(
    val coinBalance: Int = 0,
    val products: List<CoinProduct> = emptyList(),
    val isStoreAvailable: Boolean = true,
    val stats: UserStats? = null
)

sealed interface ShopEvent {
    data class PurchaseCoinPack(val activity: Activity, val product: CoinProduct) : ShopEvent
    data object RestorePurchases : ShopEvent
}

sealed interface ShopEffect {
    data object PlayClickSound : ShopEffect
    data object PlayWinSound : ShopEffect
}
