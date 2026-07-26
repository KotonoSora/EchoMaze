package com.jn.echomaze.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_history")
data class GameHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val score: Int,
    val moves: Int,
    val rewardCoins: Int,
    val levelId: Int,
    val isDailyChallenge: Boolean = false
)
