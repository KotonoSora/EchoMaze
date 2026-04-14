package com.kotonosora.echomaze.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [LevelEntity::class, CoinEntity::class, StatsEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "echo_maze_database"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Use the instance being created to seed
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                
                // Seed if empty
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = instance.gameDao()
                    if (dao.getLevelCount() == 0) {
                        seedDatabase(dao)
                    }
                }
                
                instance
            }
        }

        private suspend fun seedDatabase(dao: GameDao) {
            // Initial levels
            dao.insertLevels(listOf(
                LevelEntity(id = 1, levelNumber = 1, isUnlocked = true),
                LevelEntity(id = 2, levelNumber = 2, isUnlocked = false),
                LevelEntity(id = 3, levelNumber = 3, isUnlocked = false),
                LevelEntity(id = 4, levelNumber = 4, isUnlocked = false),
                LevelEntity(id = 5, levelNumber = 5, isUnlocked = false),
                LevelEntity(id = 6, levelNumber = 6, isUnlocked = false),
                LevelEntity(id = 7, levelNumber = 7, isUnlocked = false),
                LevelEntity(id = 8, levelNumber = 8, isUnlocked = false),
                LevelEntity(id = 9, levelNumber = 9, isUnlocked = false)
            ))
            // Initial coins
            dao.updateCoinBalance(CoinEntity(balance = 100))
            // Initial stats
            dao.updateStats(StatsEntity())
        }
    }
}
