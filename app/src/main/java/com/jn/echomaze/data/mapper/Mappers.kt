package com.jn.echomaze.data.mapper

import com.jn.echomaze.data.AchievementEntity
import com.jn.echomaze.data.GameHistoryEntity
import com.jn.echomaze.data.LevelEntity
import com.jn.echomaze.data.StatsEntity
import com.jn.echomaze.domain.model.Achievement
import com.jn.echomaze.domain.model.GameHistory
import com.jn.echomaze.domain.model.Level
import com.jn.echomaze.domain.model.UserStats

fun LevelEntity.toDomain() = Level(
    id = id,
    levelNumber = levelNumber,
    starsEarned = starsEarned,
    isUnlocked = isUnlocked,
    isCompleted = isCompleted
)

fun Level.toEntity() = LevelEntity(
    id = id,
    levelNumber = levelNumber,
    starsEarned = starsEarned,
    isUnlocked = isUnlocked,
    isCompleted = isCompleted
)

fun StatsEntity.toDomain() = UserStats(
    totalPlayTimeMillis = totalPlayTimeMillis,
    totalLevelsCompleted = totalLevelsCompleted,
    totalMovesMade = totalMovesMade,
    totalCoinsCollected = totalCoinsCollected,
    totalCoinsSpent = totalCoinsSpent,
    totalXp = totalXp,
    lastDailyRewardClaimed = lastDailyRewardClaimed,
    lastDailyChallengeCompleted = lastDailyChallengeCompleted,
    loginStreak = loginStreak,
    lastLoginTimestamp = lastLoginTimestamp
)

fun UserStats.toEntity() = StatsEntity(
    totalPlayTimeMillis = totalPlayTimeMillis,
    totalLevelsCompleted = totalLevelsCompleted,
    totalMovesMade = totalMovesMade,
    totalCoinsCollected = totalCoinsCollected,
    totalCoinsSpent = totalCoinsSpent,
    totalXp = totalXp,
    lastDailyRewardClaimed = lastDailyRewardClaimed,
    lastDailyChallengeCompleted = lastDailyChallengeCompleted,
    loginStreak = loginStreak,
    lastLoginTimestamp = lastLoginTimestamp
)

fun AchievementEntity.toDomain() = Achievement(
    id = id,
    title = title,
    description = description,
    isUnlocked = isUnlocked,
    unlockedTimestamp = unlockedTimestamp,
    rewardCoins = rewardCoins
)

fun Achievement.toEntity() = AchievementEntity(
    id = id,
    title = title,
    description = description,
    isUnlocked = isUnlocked,
    unlockedTimestamp = unlockedTimestamp,
    rewardCoins = rewardCoins
)

fun GameHistoryEntity.toDomain() = GameHistory(
    id = id,
    timestamp = timestamp,
    score = score,
    moves = moves,
    rewardCoins = rewardCoins,
    levelId = levelId,
    isDailyChallenge = isDailyChallenge
)

fun GameHistory.toEntity() = GameHistoryEntity(
    id = id,
    timestamp = timestamp,
    score = score,
    moves = moves,
    rewardCoins = rewardCoins,
    levelId = levelId,
    isDailyChallenge = isDailyChallenge
)
