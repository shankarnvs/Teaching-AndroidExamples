package com.cutm.demo.websocketsapplication

data class TradeData(
    val symbol: String,
    val price: Double,
    val quantity: Double,
    val tradeId: Long
)