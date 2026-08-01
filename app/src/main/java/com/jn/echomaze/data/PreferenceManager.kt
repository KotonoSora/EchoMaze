package com.jn.echomaze.data

import android.content.Context
import android.content.SharedPreferences
import com.jn.echomaze.domain.model.GameHistory
import com.jn.echomaze.domain.model.UserStats
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("echo_maze_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_COIN_BALANCE = "coin_balance"
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_MUSIC_ENABLED = "music_enabled"
        private const val KEY_DAILY_COMPLETED = "daily_completed"
        private const val KEY_HISTORY = "game_history"
    }

    fun getUserStats(): UserStats {
        return UserStats(
            coinBalance = sharedPreferences.getInt(KEY_COIN_BALANCE, 100),
            isSoundEnabled = sharedPreferences.getBoolean(KEY_SOUND_ENABLED, true),
            isMusicEnabled = sharedPreferences.getBoolean(KEY_MUSIC_ENABLED, true),
            lastDailyChallengeCompleted = sharedPreferences.getLong(KEY_DAILY_COMPLETED, 0L)
        )
    }

    fun getUserStatsFlow(): Flow<UserStats> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            trySend(getUserStats())
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        trySend(getUserStats())
        awaitClose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    fun updateStats(stats: UserStats) {
        sharedPreferences.edit()
            .putInt(KEY_COIN_BALANCE, stats.coinBalance)
            .putBoolean(KEY_SOUND_ENABLED, stats.isSoundEnabled)
            .putBoolean(KEY_MUSIC_ENABLED, stats.isMusicEnabled)
            .putLong(KEY_DAILY_COMPLETED, stats.lastDailyChallengeCompleted)
            .apply()
    }

    fun saveHistory(history: List<GameHistory>) {
        val serialized = history.joinToString(";") {
            "${it.timestamp}|${it.score}|${it.moves}|${it.rewardCoins}|${it.levelId}|${if (it.isDailyChallenge) 1 else 0}"
        }
        sharedPreferences.edit().putString(KEY_HISTORY, serialized).apply()
    }

    fun getHistory(): List<GameHistory> {
        val raw = sharedPreferences.getString(KEY_HISTORY, "") ?: ""
        if (raw.isEmpty()) return emptyList()

        return raw.split(";").mapNotNull { entry ->
            val parts = entry.split("|")
            if (parts.size == 6) {
                GameHistory(
                    timestamp = parts[0].toLongOrNull() ?: 0L,
                    score = parts[1].toIntOrNull() ?: 0,
                    moves = parts[2].toIntOrNull() ?: 0,
                    rewardCoins = parts[3].toIntOrNull() ?: 0,
                    levelId = parts[4].toIntOrNull() ?: 0,
                    isDailyChallenge = parts[5] == "1"
                )
            } else null
        }
    }

    fun getHistoryFlow(): Flow<List<GameHistory>> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_HISTORY) {
                trySend(getHistory())
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        trySend(getHistory())
        awaitClose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    fun reset() {
        sharedPreferences.edit().clear().apply()
    }
}
