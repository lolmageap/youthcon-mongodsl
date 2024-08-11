package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.YoungAuthor
import com.example.youthconmongodsl.collection.Status
import com.example.youthconmongodsl.extension.document
import com.example.youthconmongodsl.extension.field
import com.example.youthconmongodsl.extension.find
import org.junit.jupiter.api.BeforeEach
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
        val youngAuthor = mongoTemplate.insert(
            YoungAuthor.of(
                name = "Test",
                age = 100,
                status = Status.INACTIVE,
                books = mutableListOf(),
            )
        )

        val document = document {
            and(
                { field(YoungAuthor::id) eq youngAuthor.id },
            )
        }

        val result = mongoTemplate.find(document, YoungAuthor::class).first()
        assert(result == youngAuthor)
    }
}