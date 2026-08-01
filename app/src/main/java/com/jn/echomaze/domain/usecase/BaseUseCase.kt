package com.jn.echomaze.domain.usecase

import kotlinx.coroutines.flow.Flow

/**
 * Base interface for synchronous Use Cases.
 */
interface UseCase<in Input, out Output> {
    operator fun invoke(input: Input): Output
}

/**
 * Base interface for asynchronous Use Cases.
 */
interface SuspendUseCase<in Input, out Output> {
    suspend operator fun invoke(input: Input): Output
}

/**
 * Base interface for Use Cases that return a Flow.
 */
interface ObservableUseCase<in Input, out Output> {
    operator fun invoke(input: Input): Flow<Output>
}
