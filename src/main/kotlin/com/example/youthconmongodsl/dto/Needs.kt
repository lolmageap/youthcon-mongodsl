package com.example.youthconmongodsl.dto

import com.example.youthconmongodsl.collection.Status

data class Needs(
    val name: String? = null,
    val age: Int? = null,
    val weight: Double? = null,
    val height: Float? = null,
    val status: Status? = null,
)