package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
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

    /**
     * 1. as is 같은 경우 where(key).exists(false) <- 이러한 부분을 코틀린의 infix 함수를 사용하여 변경할 수 있을것 같다.
     *
     * 2.
     */
}