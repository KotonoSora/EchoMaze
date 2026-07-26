package com.jn.echomaze.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_stats")
data class StatsEntity(
    @PrimaryKey val id: Int = 1,
    val totalPlayTimeMillis: Long = 0,
    val totalLevelsCompleted: Int = 0,
    val totalMovesMade: Int = 0, // Replaced totalPulsesUsed
    val totalCoinsCollected: Int = 0,
    val totalCoinsSpent: Int = 0,
    val totalXp: Int = 0,
    val lastDailyRewardClaimed: Long = 0,
    val lastDailyChallengeCompleted: Long = 0,
    val loginStreak: Int = 1,
    val lastLoginTimestamp: Long = System.currentTimeMillis()
)
