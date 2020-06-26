package com.vinesh.foodie.model

import org.json.JSONArray

data class Order(

    val orderId: String,
    val orderResName: String,
    val orderDate: String,
    val orderArray: JSONArray,
    val orderCost: String

)
