package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.collection.Status
import com.example.youthconmongodsl.extension.document
import com.example.youthconmongodsl.extension.field
import com.example.youthconmongodsl.extension.find
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate

@SpringBootTest
class IdTest(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    @Test
    fun `id 값으로 조회`() {
        val author = mongoTemplate.insert(
            Author.of(
                name = "Test",
                age = 100,
                status = Status.RETIREMENT,
                books = mutableListOf(),
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