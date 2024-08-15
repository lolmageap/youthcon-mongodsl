package com.example.youthconmongodsl

import com.example.youthconmongodsl.clazz.embeddedDocument
import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.collection.Book
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
class Scenario5(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    @Test
    fun `사용자 요구사항에 맞는 검색 조건으로 조회한다 as is`() {
        val request = AuthorRequest(
            name = "Ch",
            minAge = 20,
            maxAge = 40,
            bookTitle = "Kotlin",
        )

        val criteria = mutableListOf<Criteria>()
        request.name?.let { criteria.add(Criteria.where("name").regex(it, "i")) }
        request.minAge?.let { criteria.add(Criteria.where("age").gte(it)) }
        request.maxAge?.let { criteria.add(Criteria.where("age").lte(it)) }
        request.bookTitle?.let {
            criteria.add(
                Criteria.where("books").elemMatch(Criteria.where("title").regex(it, "i"))
            )
        }
        request.status?.let { criteria.add(Criteria.where("status").`is`(it)) }

        val query = Query.query(Criteria().andOperator(*criteria.toTypedArray()))
        val authors = mongoTemplate.find(query, Author::class.java)

        // TODO : assert authors
    }

    @Test
    fun `사용자 요구사항에 맞는 검색 조건으로 조회한다 to be`() {
        val request = AuthorRequest(
            name = "Ch",
            minAge = 20,
            maxAge = 40,
            bookTitle = "Kotlin",
        )

        val document = document {
            and(
                { request.name?.let { field(Author::name) containsIgnoreCase it } },
                { field(Author::age) betweenInclusive (request.minAge to request.maxAge) },
                { request.status?.let { field(Author::status) eq it } },
            )
            embeddedDocument(Author::books).elemMatch {
                and { request.bookTitle?.let { field(Book::title) containsIgnoreCase it } }
            }
        }

        // TODO : assert authors
    }

    data class AuthorRequest(
        val name: String? = null,
        val minAge: Int? = null,
        val maxAge: Int? = null,
        val bookTitle: String? = null,
        val status: Status? = null,
    )
}