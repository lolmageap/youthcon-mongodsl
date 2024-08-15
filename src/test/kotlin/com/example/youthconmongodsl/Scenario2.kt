package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.collection.Status
import com.example.youthconmongodsl.extension.document
import com.example.youthconmongodsl.extension.field
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

@SpringBootTest
class Scenario2(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    @Test
    fun `별명이 존재하지 않는 작가 또는 나이가 50 이상이지만 키가 160cm 이하인 작가를 조회한다 as is`() {
        val criteria = Criteria().orOperator(
            Criteria.where("nickname").exists(false),
            Criteria.where("age").gte(50).and("height").lte(160),
        )
        val authors = mongoTemplate.find(Query.query(criteria), Author::class.java)

        val filteredAuthor = authors.filter { it.nickname == null || (it.age >= 50 && it.height!! <= 160) }

        assert(authors.size == filteredAuthor.size)
        assert(filteredAuthor.isNotEmpty())
    }

    @Test
    fun `별명이 존재하지 않는 작가 또는 나이가 50 이상이지만 키가 160cm 이하인 작가를 조회한다 to be`() {
        val document = document {
            or(
                { field(Author::nickname) exists false },
                {
                    and(
                        { field(Author::age) gte 50 },
                        { field(Author::height) lte 160f },
                    )
                }
            )
        }

        val authors = mongoTemplate.find(document, Author::class.java)
        val filteredAuthor = authors.filter { it.nickname == null || (it.age >= 50 && it.height!! <= 160) }

        assert(authors.size == filteredAuthor.size)
        assert(filteredAuthor.isNotEmpty())
    }

    @Test
    fun `나이가 20대 이고 상태가 ACTIVE 여야하고 또는 나이가 60대부터 80대까지, 그리고 상태가 REST, RETIREMENT 여야한다 as is`() {
        val criteria = Criteria().orOperator(
            Criteria.where("age").gte(20).lt(30).and("status").`is`(Status.ACTIVE),
            Criteria.where("age").gte(60).lt(90).and("status").`in`(Status.REST, Status.RETIREMENT),
        )

        val authors = mongoTemplate.find(Query.query(criteria), Author::class.java)

        val activeAuthor = authors.filter { it.age in 20..29 && it.status == Status.ACTIVE }
        val inactiveAuthor = authors.filter { it.age in 60..89 && it.status in listOf(Status.REST, Status.RETIREMENT) }

        assert(authors.size == activeAuthor.size + inactiveAuthor.size)
        assert(activeAuthor.isNotEmpty())
        assert(inactiveAuthor.isNotEmpty())
    }

    @Test
    fun `나이가 20대 이고 상태가 ACTIVE 여야하고 또는 나이가 60대부터 80대까지, 그리고 상태가 REST, RETIREMENT 여야한다 to be`() {
        val document = document {
            or(
                {
                    and(
                        { field(Author::age) gte 20 },
                        { field(Author::age) lt 30 },
                        { field(Author::status) eq Status.ACTIVE },
                    )
                },
                {
                    and(
                        { field(Author::age) between (59 to 90) },
                        { field(Author::status) `in` listOf(Status.REST, Status.RETIREMENT) },
                    )
                },
            )
        }

        val authors = mongoTemplate.find(document, Author::class.java)

        val activeAuthor = authors.filter { it.age in 20..29 && it.status == Status.ACTIVE }
        val inactiveAuthor = authors.filter { it.age in 60..89 && it.status in listOf(Status.REST, Status.RETIREMENT) }

        assert(authors.size == activeAuthor.size + inactiveAuthor.size)
        assert(activeAuthor.isNotEmpty())
        assert(inactiveAuthor.isNotEmpty())
    }
}