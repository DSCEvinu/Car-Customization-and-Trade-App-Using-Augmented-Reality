package com.example.carease.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pId: String,
    val name: String,
    val price: Int,
    val Image: String,
    var qua: Int
)
