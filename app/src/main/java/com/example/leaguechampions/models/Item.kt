package com.example.leaguechampions.models

data class Price(
    val base: Int,
    val total: Int,
    val sell: Int
)

data class Item(
    val name: String,
    val description: String,
    val price: Price,
    val purchasable: Boolean,
    val icon: String
)