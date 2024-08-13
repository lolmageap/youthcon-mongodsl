package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.extension.document
import com.example.youthconmongodsl.extension.field
import com.example.youthconmongodsl.extension.orderBy
import org.bson.Document
import org.junit.jupiter.api.Test

class OrderByTest {

    @Test
    fun `단일 orderBy 정렬 테스트`() {
        val document = document {
            and(
                { field(Author::name) eq "John" },
                { field(Author::age) eq 18 },
            )
        }.orderBy(Author::name).desc()

        assert(document.sortObject == Document("name", -1))
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