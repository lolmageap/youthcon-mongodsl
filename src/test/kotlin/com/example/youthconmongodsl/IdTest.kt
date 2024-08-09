package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.collection.Status
import com.example.youthconmongodsl.extension.document
import com.example.youthconmongodsl.extension.field
import com.example.youthconmongodsl.extension.find
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate

@SpringBootTest
class IdTest(
    private val mongoTemplate: MongoTemplate,
) {

    @Test
    fun `id 값으로 조회`() {
        val author = mongoTemplate.insert(
            Author(
                name = "Test",
                age = 100,
                weight = 170.0,
                height = 70f,
                status = Status.INACTIVE,
                books = emptyList(),
            )
        )

        val document = document {
            and(
                { field(Author::id) eq author.id },
            )
        }

        val result = mongoTemplate.find(document, Author::class).first()
        assert(result == author)
    }
}