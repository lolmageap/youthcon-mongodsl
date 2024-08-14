package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query.query

@SpringBootTest
class BeforeQueryDslTest(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    /**
     * 대소문자 구분 없이 별명에 hy가 포함된 저자를 조회합니다.
     *
     * 별명이 존재하지 않는 작가 또는 나이가 50 이상이지만 키가 160cm 이하인 작가를 조회한다.
     *
     * 나이가 20대 이고 상태가 ACTIVE 여야하고 또는 나이가 61살 이상 90살 미만이고 상태가 REST, RETIREMENT 여야합니다.
     *
     * nullable한 필드를 포함한 검색용 DTO로 쿼리를 작성합니다.
     *
     * 책의 제목이 Kotlin 이고 저자의 이름이 cherhy 인 책을 조회합니다.
     *
     * 책을 3권 이상 낸 30대 이상의 작가의 돈의 총합을 구합니다.
     *
     * 책을 3권 이상 낸 30대 이상의 작가를 상태 별로 그루핑하여 돈의 합을 구합니다.
     */
    @Test
    fun `대소문자 구분 없이 별명에 hy가 포함된 저자를 조회합니다`() {
        val criteria = Criteria().orOperator(
            Criteria.where("nickname").regex("hy", "i"),
        )
        val authors = mongoTemplate.find(query(criteria), Author::class.java)
        authors.filter { it.nickname?.contains("hy", ignoreCase = true) == true }

        assert(authors.isNotEmpty())
    }

    @Test
    fun `별명이 존재하지 않는 작가 또는 나이가 50 이상이지만 키가 160cm 이하인 작가를 조회한다`() {
        val criteria = Criteria().orOperator(
            Criteria.where("nickname").exists(false),
            Criteria.where("age").gte(50).and("height").lte(160),
        )
        val authors = mongoTemplate.find(query(criteria), Author::class.java)

        val author = authors.filter { it.nickname == null || (it.age >= 50 && it.height!! <= 160) }
        assert(author.isNotEmpty())
    }

}