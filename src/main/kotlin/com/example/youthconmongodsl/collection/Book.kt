package com.example.youthconmongodsl.collection

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Book(
    @Id
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val isbn: String,
)