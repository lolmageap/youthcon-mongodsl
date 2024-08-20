package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

@SpringBootTest
class Scenario2(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    @Test
    fun `대소문자 구분 없이 별명에 hy가 포함되고 나이가 30살 이상인 저자를 나이가 많은 순으로 조회한다 as is`() {
        val criteria = Criteria().andOperator(
            Criteria.where("nickname").regex("hy", "i"),
            Criteria.where("age").gte(30),
        )

        val query = Query(criteria).with(Sort.by(Sort.Order(DESC, "age")))
        val authors = mongoTemplate.find(query, Author::class.java)


        // 검증
        val nicknameFilteredAuthors = authors.filter { it.nickname?.contains("hy", ignoreCase = true) == true }
        val ageFilteredAuthors = authors.filter { it.age >= 30 }
        val maxAge = authors.maxOf { it.age }

        Assertions.assertThat(authors.size).isEqualTo(nicknameFilteredAuthors.size)
        Assertions.assertThat(authors.size).isEqualTo(ageFilteredAuthors.size)
        Assertions.assertThat(authors.first().age).isEqualTo(maxAge)
    }
}