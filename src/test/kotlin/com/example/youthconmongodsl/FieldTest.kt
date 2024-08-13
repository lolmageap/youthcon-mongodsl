package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.extension.document
import com.example.youthconmongodsl.extension.field
import org.junit.jupiter.api.Test
import org.springframework.data.mongodb.core.query.BasicQuery

class FieldTest {

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
                { field(Author::age) ne 18 }
            )
        }
        assert(document == BasicQuery("{ \"\$and\" : [{ \"name\" : {\"\$ne\" : \"John\"}}, { \"age\" : {\"\$ne\" : 18}}]}"))
    }

    @Test
    fun `큰 값을 조회합니다`() {
        val document = document {
            and(
                { field(Author::age) gt 18 },
            )
        }
        assert(document == BasicQuery("{ \"\$and\" : [{ \"age\" : {\"\$gt\" : 18}}]}"))
    }

    @Test
    fun `큰 값이거나 같은 값을 조회합니다`() {
        val document = document {
            and(
                { field(Author::age) gte 18 },
            )
        }
        assert(document == BasicQuery("{ \"\$and\" : [{ \"age\" : {\"\$gte\" : 18}}]}"))
    }

    @Test
    fun `작은 값을 조회합니다`() {
        val document = document {
            and(
                { field(Author::age) lt 18 },
            )
        }
        assert(document == BasicQuery("{ \"\$and\" : [{ \"age\" : {\"\$lt\" : 18}}]}"))
    }

    @Test
    fun `작은 값이거나 같은 값을 조회합니다`() {
        val document = document {
            and(
                { field(Author::age) lte 18 },
            )
        }
        assert(document == BasicQuery("{ \"\$and\" : [{ \"age\" : {\"\$lte\" : 18}}]}"))
    }

    @Test
    fun `사이에 있는 값을 조회합니다`() {
        val document = document {
            and(
                { field(Author::age) between (18 to 30) },
            )
        }
        assert(document == BasicQuery("{ \"\$and\" : [{ \"age\" : {\"\$gt\" : 18, \"\$lt\" : 30}}]}"))
    }

    @Test
    fun `사이에 있거나 같은 값을 조회합니다`() {
        val document = document {
            and(
                { field(Author::age) betweenInclusive (18 to 30) },
            )
        }

        assert(document == BasicQuery("{ \"\$and\" : [{ \"age\" : {\"\$gte\" : 18, \"\$lte\" : 30}}]}"))
    }

    @Test
    fun `크고 작거나 같은 값을 조회합니다`() {
        val document = document {
            and(
                { field(Author::age) gt 18 },
                { field(Author::age) lte 30 },
            )
        }
        assert(document == BasicQuery("{ \"\$and\" : [{ \"age\" : { \"\$gt\" : 18}}, { \"age\" : { \"\$lte\" : 30}}]}"))
    }

    @Test
    fun `크거나 같고 작은 값을 조회합니다 2`() {
        val document = document {
            and(
                { field(Author::age) gte 18 },
                { field(Author::age) lt 30 },
            )
        }

        assert(document == BasicQuery("{ \"\$and\" : [{ \"age\" : { \"\$gte\" : 18}}, { \"age\" : { \"\$lt\" : 30}}]}"))
    }

    @Test
    fun `값을 포함하면 조회합니다`() {
        val document = document {
            and(
                { field(Author::age) `in` listOf(18, 19, 20) },
            )
        }
        assert(document == BasicQuery("{ \"\$and\" : [{ \"age\" : {\"\$in\" : [18, 19, 20]}}]}"))
    }

    @Test
    fun `값을 포함하지 않으면 조회합니다`() {
        val document = document {
            and(
                { field(Author::age) notIn listOf(18, 19, 20) },
            )
        }
        assert(document == BasicQuery("{ \"\$and\" : [{ \"age\" : {\"\$nin\" : [18, 19, 20]}}]}"))
    }

    @Test
    fun `문자열을 포함하면 조회합니다`() {
        val document = document {
            and(
                { field(Author::name) contains "John" },
            )
        }
        assert(document == BasicQuery("{ \"\$and\" : [{ \"name\" : {\"\$regex\" : \"John\"}}]}"))
    }

    @Test
    fun `문자열을 포함하지 않으면 조회합니다`() {
        val document = document {
            and(
                { field(Author::name) containsNot "John" },
            )
        }
        assert(document == BasicQuery("{ \"\$and\" : [{ \"name\" : {\"\$not\" : {\"\$regex\" : \"John\"}}}]}"))
    }

    @Test
    fun `문자열로 시작하는 값을 조회합니다`() {
        val document = document {
            and(
                { field(Author::name) startsWith "John" },
            )
        }
        assert(document == BasicQuery("{ \"\$and\" : [{ \"name\" : {\"\$regex\" : \"^John\"}}]}"))
    }

    @Test
    fun `문자열로 끝나는 값을 조회합니다`() {
        val document = document {
            and(
                { field(Author::name) endsWith "John" },
            )
        }
        assert(document == BasicQuery("{ \"\$and\" : [{ \"name\" : {\"\$regex\" : \"John$\"}}]}"))
    }
}