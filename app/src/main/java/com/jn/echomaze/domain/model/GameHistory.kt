package com.jn.echomaze.domain.model

data class GameHistory(
    val id: Int = 0,
    val timestamp: Long,
    val score: Int,
    val moves: Int,
    val rewardCoins: Int,
    val levelId: Int, // 0 for daily, otherwise just a tracking id
    val isDailyChallenge: Boolean = false
)
