package com.example.youthconmongodsl.collection

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "author")
data class YoungAuthor(
    @Id @Field("_id")
    val id: String = ObjectId.get().toHexString(),
    val name: String,
    val nickname: String,
    val age: Int,
    val weight: Double,
    val height: Float,
    val status: Status,
    val books: MutableList<String> = mutableListOf(),
)