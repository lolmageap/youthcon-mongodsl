package com.example.youthconmongodsl

import com.example.youthconmongodsl.clazz.embeddedDocument
import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.collection.Book
import com.example.youthconmongodsl.extension.document
import com.example.youthconmongodsl.extension.field
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

@SpringBootTest
class Scenario3(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    @Test
    fun `cherhy라는 저자가 쓴 책 중에서 제목이 Kotlin이 대소문자 구분하지 않은 채로 포함되어 있으면 조회한다 as is`() {
        val criteria = Criteria().andOperator(
            Criteria.where("name").`is`("cherhy"),
            Criteria.where("books").elemMatch(
                Criteria.where("title").regex("Kotlin", "i")
            ),
        )

        val authors = mongoTemplate.find(Query.query(criteria), Author::class.java)
        val author = authors.filter { author ->
            author.books.any { it.title.contains("Kotlin", ignoreCase = true) } && author.name == "cherhy"
        }

        assert(authors.size == author.size)
        assert(author.isNotEmpty())
    }

    @Test
    fun `cherhy라는 저자가 쓴 책 중에서 제목이 Kotlin이 대소문자 구분하지 않은 채로 포함되어 있으면 조회한다 to be`() {
        val document = document {
            and { field(Author::name) eq "cherhy" }

            embeddedDocument(Author::books).elemMatch {
                and { field(Book::title) containsIgnoreCase "Kotlin" }
            }
        }

        val authors = mongoTemplate.find(document, Author::class.java)
        val author = authors.filter { author ->
            author.books.any { it.title.contains("Kotlin", ignoreCase = true) } && author.name == "cherhy"
        }

        assert(authors.size == author.size)
        assert(author.isNotEmpty())
    }
}