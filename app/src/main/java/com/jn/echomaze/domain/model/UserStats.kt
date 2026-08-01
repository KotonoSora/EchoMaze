package com.jn.echomaze.domain.model

data class UserStats(
    val coinBalance: Int = 100,
    val isSoundEnabled: Boolean = true,
    val isMusicEnabled: Boolean = true,
    val lastDailyChallengeCompleted: Long = 0
)
