package com.jn.echomaze.domain.usecase

import com.jn.echomaze.domain.model.Puzzle

data class HandleMoveInput(
    val puzzle: Puzzle,
    val clickedIndex: Int
)

class HandleMoveUseCase : UseCase<HandleMoveInput, Puzzle> {
    override fun invoke(input: HandleMoveInput): Puzzle {
        val puzzle = input.puzzle
        val clickedIndex = input.clickedIndex

        if (puzzle.isSolved) return puzzle

        val emptyIndex = puzzle.tiles.indexOf(0)
        val gridSize = puzzle.gridSize

        if (isMoveValid(clickedIndex, emptyIndex, gridSize)) {
            val newTiles = puzzle.tiles.toMutableList()
            newTiles[emptyIndex] = newTiles[clickedIndex]
            newTiles[clickedIndex] = 0

            val isSolved = checkSolved(newTiles)

            return puzzle.copy(
                tiles = newTiles,
                moves = puzzle.moves + 1,
                isSolved = isSolved
            )
        }

        return puzzle
    }

    private fun isMoveValid(clickedIndex: Int, emptyIndex: Int, gridSize: Int): Boolean {
        val row1 = clickedIndex / gridSize
        val col1 = clickedIndex % gridSize
        val row2 = emptyIndex / gridSize
        val col2 = emptyIndex % gridSize

        return (kotlin.math.abs(row1 - row2) == 1 && col1 == col2) ||
                (row1 == row2 && kotlin.math.abs(col1 - col2) == 1)
    }

    private fun checkSolved(tiles: List<Int>): Boolean {
        for (i in 0 until tiles.size - 1) {
            if (tiles[i] != i + 1) return false
        }
        return tiles.last() == 0
    }
}
