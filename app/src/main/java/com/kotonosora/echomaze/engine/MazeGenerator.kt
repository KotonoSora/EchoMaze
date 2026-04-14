package com.kotonosora.echomaze.engine

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlin.random.Random

data class MazeWall(val bounds: Rect)
data class MazeData(
    val walls: List<MazeWall>,
    val startPos: Offset,
    val exitPos: Offset,
    val width: Float,
    val height: Float
)

class MazeGenerator {
    fun generateMaze(width: Float, height: Float, level: Int): MazeData {
        val walls = mutableListOf<MazeWall>()
        val random = Random(level.toLong())
        
        // Add border walls
        val thickness = 20f
        walls.add(MazeWall(Rect(0f, 0f, width, thickness))) // Top
        walls.add(MazeWall(Rect(0f, height - thickness, width, height))) // Bottom
        walls.add(MazeWall(Rect(0f, 0f, thickness, height))) // Left
        walls.add(MazeWall(Rect(width - thickness, 0f, width, height))) // Right

        // Simple random wall generation based on level
        val numWalls = 10 + level * 2
        for (i in 0 until numWalls) {
            val isHorizontal = random.nextBoolean()
            val w = if (isHorizontal) random.nextFloat() * (width * 0.4f) + 100f else thickness
            val h = if (isHorizontal) thickness else random.nextFloat() * (height * 0.4f) + 100f
            val x = random.nextFloat() * (width - w - thickness * 2) + thickness
            val y = random.nextFloat() * (height - h - thickness * 2) + thickness
            
            val wallRect = Rect(x, y, x + w, y + h)
            // Don't place walls in start/end areas
            if (wallRect.overlaps(Rect(0f, 0f, 200f, 200f)) || 
                wallRect.overlaps(Rect(width - 200f, height - 200f, width, height))) {
                continue
            }
            walls.add(MazeWall(wallRect))
        }

        return MazeData(
            walls = walls,
            startPos = Offset(100f, 100f),
            exitPos = Offset(width - 100f, height - 100f),
            width = width,
            height = height
        )
    }
}
