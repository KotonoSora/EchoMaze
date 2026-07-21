package com.jn.echomaze.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "levels")
data class LevelEntity(
    @PrimaryKey val id: Int,
    val levelNumber: Int,
    val starsEarned: Int = 0,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false
)
