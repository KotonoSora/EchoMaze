package com.jn.echomaze.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coin_balance")
data class CoinEntity(
    @PrimaryKey val id: Int = 1,
    val balance: Int = 0
)
