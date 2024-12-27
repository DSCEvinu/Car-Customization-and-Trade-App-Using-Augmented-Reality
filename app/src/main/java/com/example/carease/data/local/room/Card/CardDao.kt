package com.example.carease.data.local.room.Card

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface CardDao {
    @Query("SELECT * FROM Card_List ORDER BY id ASC")
    fun getAll(): LiveData<List<CardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(card: CardEntity)

    @Delete
    fun delete(card: CardEntity)

    @Update
    fun update(card: CardEntity)
}
