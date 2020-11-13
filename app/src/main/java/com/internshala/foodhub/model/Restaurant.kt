package com.internshala.foodhub.model

import java.io.Serializable

data class Restaurant(
    val id: Int,
    val name: String,
    val rating: String,
    val costForTwo: Int,
    val imageUrl: String
)