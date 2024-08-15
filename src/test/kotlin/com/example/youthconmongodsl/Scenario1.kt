package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.extension.document
import com.example.youthconmongodsl.extension.field
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

@SpringBootTest
class Scenario1(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    @Test
    fun `대소문자 구분 없이 별명에 hy가 포함된 저자를 조회합니다 as is`() {
        val criteria = Criteria().andOperator(
            Criteria.where("nickname").regex("hy", "i"),
        )
        val authors = mongoTemplate.find(Query.query(criteria), Author::class.java)
        val filteredAuthor = authors.filter { it.nickname?.contains("hy", ignoreCase = true) == true }

        assert(authors.size == filteredAuthor.size)
        assert(authors.isNotEmpty())
    }

    @Test
    fun `대소문자 구분 없이 별명에 hy가 포함된 저자를 조회합니다 to be`() {
        val document = document {
            and { field(Author::nickname) containsIgnoreCase "hy" }
        }

        val authors = mongoTemplate.find(document, Author::class.java)
        val filteredAuthor = authors.filter { it.nickname?.contains("hy", ignoreCase = true) == true }

        assert(authors.size == filteredAuthor.size)
        assert(authors.isNotEmpty())
    }
}