package com.jn.echomaze.domain.model

data class GameHistory(
    val id: Long = 0,
    val timestamp: Long = 0,
    val score: Int,
    val moves: Int,
    val rewardCoins: Int,
    val levelId: Int,
    val isDailyChallenge: Boolean = false
)
