package com.jn.echomaze.di

import android.content.Context
import com.jn.echomaze.billing.BillingManager
import com.jn.echomaze.data.AppDatabase
import com.jn.echomaze.data.repository.OfflineGameRepository
import com.jn.echomaze.domain.repository.GameRepository
import com.jn.echomaze.domain.usecase.CheckAchievementsUseCase
import com.jn.echomaze.domain.usecase.GetPuzzleUseCase
import com.jn.echomaze.domain.usecase.HandleMoveUseCase
import com.jn.echomaze.domain.usecase.ProcessLevelCompletionUseCase
import com.jn.echomaze.engine.SoundManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val gameRepository: GameRepository
    val billingManager: BillingManager
    val soundManager: SoundManager
    val getPuzzleUseCase: GetPuzzleUseCase
    val handleMoveUseCase: HandleMoveUseCase
    val checkAchievementsUseCase: CheckAchievementsUseCase
    val processLevelCompletionUseCase: ProcessLevelCompletionUseCase
}

/**
 * [AppContainer] implementation that provides instance of [OfflineGameRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    /**
     * Implementation for [GameRepository]
     */
    override val gameRepository: GameRepository by lazy {
        OfflineGameRepository(AppDatabase.getDatabase(context).gameDao())
    }

    override val billingManager: BillingManager by lazy {
        BillingManager(context, applicationScope).apply {
            startConnection()
        }
    }

    override val soundManager: SoundManager by lazy {
        SoundManager(context)
    }

    override val getPuzzleUseCase: GetPuzzleUseCase by lazy {
        GetPuzzleUseCase()
    }

    override val handleMoveUseCase: HandleMoveUseCase by lazy {
        HandleMoveUseCase()
    }

    override val checkAchievementsUseCase: CheckAchievementsUseCase by lazy {
        CheckAchievementsUseCase(gameRepository)
    }

    override val processLevelCompletionUseCase: ProcessLevelCompletionUseCase by lazy {
        ProcessLevelCompletionUseCase(gameRepository, checkAchievementsUseCase)
    }
}
