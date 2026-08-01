package com.jn.echomaze.domain.usecase

import com.jn.echomaze.domain.model.Puzzle
import kotlin.random.Random

data class GetPuzzleInput(
    val gridSize: Int,
    val seed: Int,
    val imageRes: Int? = null
)

class GetPuzzleUseCase : UseCase<GetPuzzleInput, Puzzle> {
    override fun invoke(input: GetPuzzleInput): Puzzle {
        val random = Random(input.seed)
        val initialTiles = (1 until input.gridSize * input.gridSize).toList() + listOf(0)

        val tiles = initialTiles.toMutableList()
        var emptyIndex = tiles.indexOf(0)

        // Shuffle by making valid moves from a solved state to ensure solvability
        val shuffleMoves = input.gridSize * input.gridSize * 20

        repeat(shuffleMoves) {
            val neighbors = getNeighbors(emptyIndex, input.gridSize)
            val moveToIndex = neighbors[random.nextInt(neighbors.size)]

            // Swap
            tiles[emptyIndex] = tiles[moveToIndex]
            tiles[moveToIndex] = 0
            emptyIndex = moveToIndex
        }

        return Puzzle(
            tiles = tiles,
            gridSize = input.gridSize,
            moves = 0,
            isSolved = false,
            imageRes = input.imageRes,
            seed = input.seed
        )
    }

    private fun getNeighbors(index: Int, gridSize: Int): List<Int> {
        val neighbors = mutableListOf<Int>()
        val row = index / gridSize
        val col = index % gridSize

        if (row > 0) neighbors.add(index - gridSize)
        if (row < gridSize - 1) neighbors.add(index + gridSize)
        if (col > 0) neighbors.add(index - 1)
        if (col < gridSize - 1) neighbors.add(index + 1)

        return neighbors
    }
}
