package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.YoungAuthor
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
        val youngAuthor = mongoTemplate.insert(
            YoungAuthor(
                name = "Test",
                age = 100,
                nickname = "nickname",
                weight = 170.0,
                height = 70f,
                status = Status.INACTIVE,
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