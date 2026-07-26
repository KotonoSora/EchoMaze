package com.jn.echomaze.domain.model

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val isUnlocked: Boolean = false,
    val unlockedTimestamp: Long = 0,
    val rewardCoins: Int = 100
)
