package com.example.carease.data.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Car(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val description: String = ""
)

