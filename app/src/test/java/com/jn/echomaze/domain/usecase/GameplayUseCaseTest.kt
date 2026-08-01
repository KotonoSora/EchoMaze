package com.jn.echomaze.domain.usecase

import com.jn.echomaze.domain.model.Puzzle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameplayUseCaseTest {

    private val getPuzzleUseCase = GetPuzzleUseCase()
    private val handleMoveUseCase = HandleMoveUseCase()

    @Test
    fun `getPuzzleUseCase generates a valid puzzle`() {
        val gridSize = 3
        val seed = 123
        val puzzle = getPuzzleUseCase(GetPuzzleInput(gridSize, seed))

        assertEquals(gridSize, puzzle.gridSize)
        assertEquals(gridSize * gridSize, puzzle.tiles.size)
        assertTrue(puzzle.tiles.contains(0))
        assertFalse(puzzle.isSolved)
        assertEquals(0, puzzle.moves)
    }

    @Test
    fun `handleMoveUseCase performs a valid swap`() {
        // Create a nearly solved puzzle: [1, 2, 3, 4, 5, 6, 7, 0, 8]
        val puzzle = Puzzle(
            tiles = listOf(1, 2, 3, 4, 5, 6, 7, 0, 8),
            gridSize = 3,
            moves = 0,
            isSolved = false
        )

        // Click index 8 (value 8) - it's next to 0 (index 7)
        val newPuzzle = handleMoveUseCase(HandleMoveInput(puzzle, 8))

        assertNotEquals(puzzle, newPuzzle)
        assertEquals(1, newPuzzle.moves)
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8, 0), newPuzzle.tiles)
        assertTrue(newPuzzle.isSolved)
    }

    @Test
    fun `handleMoveUseCase ignores invalid moves`() {
        // [1, 2, 3, 4, 5, 6, 7, 0, 8] - 0 is at index 7
        val puzzle = Puzzle(
            tiles = listOf(1, 2, 3, 4, 5, 6, 7, 0, 8),
            gridSize = 3
        )

        // Click index 0 (top left) - not adjacent to index 7
        val newPuzzle = handleMoveUseCase(HandleMoveInput(puzzle, 0))

        assertEquals(puzzle, newPuzzle)
        assertEquals(0, newPuzzle.moves)
    }
}
