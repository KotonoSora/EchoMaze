package com.jn.echomaze.domain.model

data class UserStats(
    val totalPlayTimeMillis: Long = 0,
    val totalLevelsCompleted: Int = 0,
    val totalMovesMade: Int = 0,
    val totalCoinsCollected: Int = 0,
    val totalCoinsSpent: Int = 0,
    val totalXp: Int = 0,
    val lastDailyRewardClaimed: Long = 0,
    val lastDailyChallengeCompleted: Long = 0,
    val loginStreak: Int = 1,
    val lastLoginTimestamp: Long = 0
)
