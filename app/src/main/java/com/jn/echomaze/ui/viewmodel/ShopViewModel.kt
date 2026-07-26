package com.jn.echomaze.ui.viewmodel

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jn.echomaze.billing.BillingManager
import com.jn.echomaze.billing.CoinProduct
import com.jn.echomaze.domain.repository.GameRepository
import com.jn.echomaze.engine.SoundManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShopViewModel(
    private val gameRepository: GameRepository,
    val billingManager: BillingManager,
    private val soundManager: SoundManager
) : ViewModel() {

    val coinBalance: StateFlow<Int> = gameRepository.getCoinBalance()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val products: StateFlow<List<CoinProduct>> = billingManager.productsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isStoreAvailable: StateFlow<Boolean> = billingManager.isServiceAvailable
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    var adCooldownSeconds by mutableLongStateOf(0L)
        private set

    init {
        startAdCooldownTimer()
        observePurchases()
    }

    private fun startAdCooldownTimer() {
        viewModelScope.launch {
            while (true) {
                if (adCooldownSeconds > 0) {
                    adCooldownSeconds--
                }
                delay(1000)
            }
        }
    }

    private fun observePurchases() {
        viewModelScope.launch {
            billingManager.purchasesFlow.collect { purchases ->
                if (purchases.isNotEmpty()) {
                    addCoins(1000)
                    soundManager.playWin()
                } else {
                    // Mock award for testing
                    addCoins(500)
                }
            }
        }
    }

    fun watchAdForCoins() {
        if (adCooldownSeconds == 0L) {
            viewModelScope.launch {
                soundManager.playClick()
                addCoins(50)
                adCooldownSeconds = 3600 // 1 hour cooldown
            }
        }
    }

    private suspend fun addCoins(amount: Int) {
        val currentBalance = gameRepository.getCoinBalance().first()
        gameRepository.updateCoinBalance(currentBalance + amount)
    }

    fun buyUpgrade(price: Int, upgradeId: String) {
        viewModelScope.launch {
            val balance = gameRepository.getCoinBalance().first()
            val cost = when (upgradeId) {
                "hint" -> 300
                "undo" -> 150
                else -> price
            }
            if (balance >= cost) {
                soundManager.playClick()
                gameRepository.updateCoinBalance(balance - cost)
            }
        }
    }

    fun purchaseCoinPack(activity: Activity, product: CoinProduct) {
        soundManager.playClick()
        billingManager.launchBillingFlow(activity, product)
    }
}
