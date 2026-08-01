package com.jn.echomaze.domain.usecase

import com.jn.echomaze.domain.model.GameHistory
import com.jn.echomaze.domain.repository.GameRepository
import kotlinx.coroutines.flow.first

data class ProcessGameCompletionInput(
    val score: Int,
    val moves: Int,
    val isDailyChallenge: Boolean
)

class ProcessGameCompletionUseCase(
    private val repository: GameRepository
) : SuspendUseCase<ProcessGameCompletionInput, Unit> {

    override suspend fun invoke(input: ProcessGameCompletionInput) {
        val rewardCoins = if (input.isDailyChallenge) 200 else 50
        val now = System.currentTimeMillis()

        // Add to history
        repository.insertHistory(
            GameHistory(
                timestamp = now,
                score = input.score,
                moves = input.moves,
                rewardCoins = rewardCoins,
                levelId = if (input.isDailyChallenge) 0 else 1,
                isDailyChallenge = input.isDailyChallenge
            )
        )

        // Update stats
        val stats = repository.getUserStats().first()
        repository.updateStats(
            stats.copy(
                coinBalance = stats.coinBalance + rewardCoins,
                lastDailyChallengeCompleted = if (input.isDailyChallenge) now else stats.lastDailyChallengeCompleted
            )
        )
    }
}
