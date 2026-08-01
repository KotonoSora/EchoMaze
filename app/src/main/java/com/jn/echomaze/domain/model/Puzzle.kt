package com.jn.echomaze.domain.model

data class Puzzle(
    val tiles: List<Int>, // 0 represents the empty slot
    val gridSize: Int,
    val moves: Int = 0,
    val isSolved: Boolean = false,
    val imageRes: Int? = null,
    val seed: Int = 0
)
