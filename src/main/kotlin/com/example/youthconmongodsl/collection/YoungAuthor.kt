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
    val age: Int,
    val status: Status,
    val nickname: String?,
    val weight: Double?,
    val height: Float?,
    val books: MutableList<Book>,
) {
    companion object {
        fun of(
            name: String,
            age: Int,
            books: MutableList<Book>,
            status: Status = Status.ACTIVE,
            nickname: String? = null,
            weight: Double? = null,
            height: Float? = null,
        ) =
            YoungAuthor(
                name = name,
                age = age,
                books = books,
                status = status,
                nickname = nickname,
                weight = weight,
                height = height,
            )
    }
}