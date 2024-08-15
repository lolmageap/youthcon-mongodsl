package com.example.youthconmongodsl

import com.example.youthconmongodsl.clazz.embeddedDocument
import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.collection.Book
import com.example.youthconmongodsl.extension.document
import com.example.youthconmongodsl.extension.field
import com.example.youthconmongodsl.extension.orderBy
import com.example.youthconmongodsl.extension.sum
import org.bson.Document
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.BasicQuery
import kotlin.test.Test

@SpringBootTest
class Grammar(
    private val mongoTemplate: MongoTemplate,
) {
    @Test
    fun `같은 값을 조회합니다`() {
        val document = document {
            and(
                { field(Author::name) eq "John" },
                { field(Author::age) eq 18 },
            )
        }

        assert(document == BasicQuery("{ \"\$and\" : [{ \"name\" : \"John\"}, { \"age\" : 18}]}"))
    }

    @Test
    fun `같지 않은 값을 조회합니다`() {
        val document = document {
            and(
                { field(Author::name) ne "John" },
                { field(Author::age) ne 18 },
            )
        }

        assert(document == BasicQuery("{ \"\$and\" : [{ \"name\" : {\"\$ne\" : \"John\"}}, { \"age\" : {\"\$ne\" : 18}}]}"))
    }

    @Test
    fun `큰 값을 조회합니다`() {
        val document = document {
            and { field(Author::age) gt 18 }
        }

        assert(document == BasicQuery("{ \"\$and\" : [{ \"age\" : {\"\$gt\" : 18}}]}"))
    }

    @Test
    fun `작은 값을 조회합니다`() {
        val document = document {
            and { field(Author::age) lt 18 }
        }

        assert(document == BasicQuery("{ \"\$and\" : [{ \"age\" : {\"\$lt\" : 18}}]}"))
    }

    @Test
    fun `사이에 있는 값을 조회합니다`() {
        val document = document {
            and { field(Author::age) between (18 to 30) }
        }

        assert(document == BasicQuery("{ \"\$and\" : [{ \"age\" : {\"\$gte\" : 18, \"\$lte\" : 30}}]}"))
    }

    @Test
    fun `값을 포함하면 조회합니다`() {
        val document = document {
            and { field(Author::age) `in` listOf(18, 19, 20) }
        }

        assert(document == BasicQuery("{ \"\$and\" : [{ \"age\" : {\"\$in\" : [18, 19, 20]}}]}"))
    }

    @Test
    fun `like 연산과 같이 문자열을 포함하면 조회합니다`() {
        val document = document {
            and { field(Author::name) contains "John" }
        }

        assert(document == BasicQuery("{ \"\$and\" : [{ \"name\" : {\"\$regex\" : \"John\"}}]}"))
    }

    @Test
    fun `like 연산과 같지만 대소문자를 구별하지 않고 문자열을 포함하면 조회합니다`() {
        val document = document {
            and { field(Author::name) containsIgnoreCase "John" }
        }

        assert(document == BasicQuery("{ \"\$and\" : [{ \"name\" : {\"\$regex\" : \"John\", \"\$options\" : \"i\"}}]}"))
    }

    @Test
    fun `Author 내부 필드인 book 객체 중 title 필드가 "book1"인 Author를 조회합니다`() {
        val document = document {
            embeddedDocument(Author::books).elemMatch {
                and { field(Book::title) eq "book1" }
            }
        }

        assert(document == BasicQuery("{ \"books\" : {\"\$elemMatch\" : {\"title\" : \"book1\"}}}"))
    }

    @Test
    fun `전체에 대한 합을 구합니다`() {
        val document = document {
            and { field(Author::name) eq "John" }
        }

        val sumOfAge = mongoTemplate.sum(document, Author::age)
        assert(sumOfAge == 100)
    }

    @Test
    fun `다중 orderBy 정렬 테스트`() {
        val document = document {
            and(
                { field(Author::name) eq "John" },
                { field(Author::age) eq 18 },
            )
        }.orderBy(Author::name).desc()
            .orderBy(Author::age).asc()

        assert(document.sortObject == Document("name", -1).append("age", 1))
    }
}