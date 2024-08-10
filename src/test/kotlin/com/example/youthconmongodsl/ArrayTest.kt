package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.YoungAuthor
import com.example.youthconmongodsl.extension.document
import com.example.youthconmongodsl.extension.field
import com.example.youthconmongodsl.extension.find
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate

@SpringBootTest
class ArrayTest(
    @Autowired private val mongoTemplate: MongoTemplate,
) {

    @Test
    fun `배열 필드에 대한 equal 연산 테스트`() {
        val document = document {
            and(
                { field(YoungAuthor::books) eq mutableListOf("book1", "book2") },
            )
        }

        val youngAuthor = mongoTemplate.find(document, YoungAuthor::class).first()
        assert(youngAuthor.books == mutableListOf("book1", "book2"))
    }

    @Test
    fun `배열 필드에 대한 not equal 연산 테스트`() {
        val document = document {
            and(
                { field(YoungAuthor::books) ne mutableListOf("book1", "book2") },
            )
        }

        val youngAuthor = mongoTemplate.find(document, YoungAuthor::class).first()
        assert(youngAuthor.books != mutableListOf("book1", "book2"))
    }
}