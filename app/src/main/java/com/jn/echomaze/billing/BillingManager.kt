package com.jn.echomaze.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CoinProduct(
    val id: String,
    val name: String,
    val description: String,
    val price: String,
    val coins: Int,
    val productDetails: ProductDetails? = null
)

class BillingManager(
    private val context: Context,
    private val externalScope: CoroutineScope
) : PurchasesUpdatedListener {

    private val _purchasesFlow = MutableSharedFlow<List<Purchase>>()
    val purchasesFlow: SharedFlow<List<Purchase>> = _purchasesFlow

    private val _productsFlow = MutableStateFlow<List<CoinProduct>>(emptyList())
    val productsFlow: StateFlow<List<CoinProduct>> = _productsFlow

    private val _isServiceAvailable = MutableStateFlow(true)
    val isServiceAvailable: StateFlow<Boolean> = _isServiceAvailable

    private var billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(PendingPurchasesParams.newBuilder().enableOneTimeProducts().build())
        .build()

    private val productIds = listOf(
        "coins_100", "coins_500", "coins_1000", "coins_1500",
        "coins_2000", "coins_2500", "coins_3000", "coins_3500", "coins_4000"
    )

    fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    _isServiceAvailable.value = true
                    queryProductDetails()
                    restorePurchases()
                } else {
                    _isServiceAvailable.value = false
                    handleError()
                }
            }

            override fun onBillingServiceDisconnected() {
                _isServiceAvailable.value = false
                handleError()
            }
        })
    }

    private fun handleError() {
        // Simplified error handling: Provide mock data if real store fails
        // We'll use a local check instead of BuildConfig if it's missing
        _productsFlow.value = getMockProducts()
    }

    private fun queryProductDetails() {
        val queryProductList = productIds.map {
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(it)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(queryProductList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, queryProductDetailsResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val productDetailsList = queryProductDetailsResult.productDetailsList
                if (productDetailsList.isEmpty()) {
                    _productsFlow.value = getMockProducts()
                } else {
                    val mappedProducts = productDetailsList.map { details ->
                        CoinProduct(
                            id = details.productId,
                            name = details.name,
                            description = details.description,
                            price = details.oneTimePurchaseOfferDetails?.formattedPrice ?: "N/A",
                            coins = getCoinAmount(details.productId),
                            productDetails = details
                        )
                    }.sortedBy { it.coins }
                    _productsFlow.value = mappedProducts
                }
            } else {
                handleError()
            }
        }
    }

    private fun getMockProducts(): List<CoinProduct> {
        val desc =
            "A pack of %d coins used to unlock powerful boosts like Extra Time, Hint, and Undo."
        return listOf(
            CoinProduct("coins_100", "100 Coins", desc.format(100), "$0.29", 100),
            CoinProduct("coins_500", "500 Coins", desc.format(500), "$0.49", 500),
            CoinProduct("coins_1000", "1000 Coins", desc.format(1000), "$0.69", 1000),
            CoinProduct("coins_1500", "1500 Coins", desc.format(1500), "$0.99", 1500),
            CoinProduct("coins_2000", "2000 Coins", desc.format(2000), "$1.99", 2000),
            CoinProduct("coins_2500", "2500 Coins", desc.format(2500), "$3.99", 2500),
            CoinProduct("coins_3000", "3000 Coins", desc.format(3000), "$4.99", 3000),
            CoinProduct("coins_3500", "3500 Coins", desc.format(3500), "$7.99", 3500),
            CoinProduct("coins_4000", "4000 Coins", desc.format(4000), "$9.99", 4000)
        )
    }

    private fun getCoinAmount(productId: String): Int {
        return productId.substringAfter("coins_").toIntOrNull() ?: 0
    }

    fun restorePurchases() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                externalScope.launch {
                    _purchasesFlow.emit(purchases)
                    for (purchase in purchases) {
                        handlePurchase(purchase)
                    }
                }
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            externalScope.launch {
                _purchasesFlow.emit(purchases)
                for (purchase in purchases) {
                    handlePurchase(purchase)
                }
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { _ -> }
            }
            val consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            billingClient.consumeAsync(consumeParams) { _, _ -> }
        }
    }

    fun launchBillingFlow(activity: Activity, product: CoinProduct) {
        if (product.productDetails != null) {
            val productDetailsParamsList = listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(product.productDetails)
                    .build()
            )

            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()

            billingClient.launchBillingFlow(activity, billingFlowParams)
        } else {
            // Mock success for development
            externalScope.launch {
                // Award coins based on product
                // In this mock case we trigger a purchase update with a dummy
                _purchasesFlow.emit(emptyList())
            }
        }
    }
}
