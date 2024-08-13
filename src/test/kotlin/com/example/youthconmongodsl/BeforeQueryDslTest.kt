package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.collection.QOldAuthor
import com.example.youthconmongodsl.extension.document
import com.example.youthconmongodsl.extension.field
import com.example.youthconmongodsl.extension.find
import com.example.youthconmongodsl.extension.orderBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate

@SpringBootTest
class BeforeQueryDslTest(
    @Autowired private val mongoTemplate: MongoTemplate,
) {

    @Test
    fun `CustomDSL로 or 조건 내부에서 and 조건으로 묶어 조회한다`() {
        val document = document {
            or(
                {
                    and(
                        { field(Author::name) eq "John" },
                        { field(Author::age) eq 18 },
                    )
                },
                {
                    and(
                        { field(Author::name) eq "Any" },
                        { field(Author::age) eq 81 },
                    )
                },
            )
        }.orderBy(Author::age).desc()
            .orderBy(Author::name).asc()

        mongoTemplate.find(document, Author::class)
    }
}