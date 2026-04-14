package com.kotonosora.echomaze.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_stats")
data class StatsEntity(
    @PrimaryKey val id: Int = 1,
    val totalPlayTimeMillis: Long = 0,
    val totalLevelsCompleted: Int = 0,
    val totalPulsesUsed: Int = 0,
    val totalCoinsCollected: Int = 0,
    val totalCoinsSpent: Int = 0
)
