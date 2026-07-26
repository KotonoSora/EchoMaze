package com.jn.echomaze.domain.model

data class Level(
    val id: Int,
    val levelNumber: Int,
    val starsEarned: Int = 0,
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false
)
