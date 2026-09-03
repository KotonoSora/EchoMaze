package com.jn.echomaze.ui.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.Purchase
import com.jn.echomaze.billing.BillingManager
import com.jn.echomaze.billing.CoinProduct
import com.jn.echomaze.domain.repository.GameRepository
import com.jn.echomaze.engine.SoundManager
import com.jn.echomaze.ui.viewmodel.shop.ShopEffect
import com.jn.echomaze.ui.viewmodel.shop.ShopEvent
import com.jn.echomaze.ui.viewmodel.shop.ShopUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShopViewModel(
    private val gameRepository: GameRepository,
    val billingManager: BillingManager,
    private val soundManager: SoundManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShopUiState())
    val uiState: StateFlow<ShopUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<ShopEffect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        observeData()
        observePurchases()
    }

    private fun observeData() {
        viewModelScope.launch {
            gameRepository.getUserStats().collectLatest { stats ->
                _uiState.update {
                    it.copy(
                        coinBalance = stats.coinBalance,
                        stats = stats
                    )
                }
            }
        }

        viewModelScope.launch {
            billingManager.productsFlow.collectLatest { products ->
                _uiState.update { it.copy(products = products) }
            }
        }

        viewModelScope.launch {
            billingManager.isServiceAvailable.collectLatest { available ->
                _uiState.update { it.copy(isStoreAvailable = available) }
            }
        }
    }

    private fun observePurchases() {
        viewModelScope.launch {
            billingManager.purchasesFlow.collect { purchases ->
                val soundEnabled = _uiState.value.stats?.isSoundEnabled ?: true
                for (purchase in purchases) {
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        val productId = purchase.products.firstOrNull() ?: ""
                        val coins = billingManager.getCoinAmount(productId)
                        val coinAmount = if (coins > 0) coins else 1000
                        addCoins(coinAmount)
                        if (soundEnabled) {
                            emitEffect(ShopEffect.PlayWinSound)
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: ShopEvent) {
        when (event) {
            is ShopEvent.PurchaseCoinPack -> purchaseCoinPack(event.activity, event.product)
            ShopEvent.RestorePurchases -> billingManager.restorePurchases()
        }
    }

    private suspend fun addCoins(amount: Int) {
        val currentStats = gameRepository.getUserStats().first()
        gameRepository.updateStats(currentStats.copy(coinBalance = currentStats.coinBalance + amount))
    }

    private fun purchaseCoinPack(activity: Activity, product: CoinProduct) {
        val soundEnabled = _uiState.value.stats?.isSoundEnabled ?: true
        if (soundEnabled) {
            emitEffect(ShopEffect.PlayClickSound)
        }
        viewModelScope.launch {
            val launched = billingManager.launchBillingFlow(activity, product)
            if (!launched) {
                // Development / fallback purchase mode
                addCoins(product.coins)
                if (soundEnabled) {
                    emitEffect(ShopEffect.PlayWinSound)
                }
            }
        }
    }

    private fun emitEffect(effect: ShopEffect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}
