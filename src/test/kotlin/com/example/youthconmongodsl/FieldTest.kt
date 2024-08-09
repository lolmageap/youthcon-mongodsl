package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.extension.document
import com.example.youthconmongodsl.extension.field
import org.junit.jupiter.api.Test

class FieldTest {

    @Test
    fun `equal 연산 테스트`() {
        val document = document {
            and(
                { field(Author::name) eq "John" },
                { field(Author::age) eq 18 },
            )
        }

        assert(document.toString() == "{ \"\$and\" : [{ \"name\" : \"John\"}, { \"age\" : 18}]}")
    }

    @Test
    fun `not equal 연산 테스트`() {
        val document = document {
            and(
                { field(Author::name) ne "John" },
                { field(Author::age) ne 18 }
            )
        }

    }

    @Test
    fun `greater than 연산 테스트`() {
        val document = document {
            and(
                { field(Author::age) gt 18 },
            )
        }

    }

    @Test
    fun `greater than or equal 연산 테스트`() {
        val document = document {
            and(
                { field(Author::age) gte 18 },
            )
        }

    }

    @Test
    fun `less than 연산 테스트`() {
        val document = document {
            and(
                { field(Author::age) lt 18 },
            )
        }

    }

    @Test
    fun `less than or equal 연산 테스트`() {
        val document = document {
            and(
                { field(Author::age) lte 18 },
            )
        }

    }

    @Test
    fun `between 연산 테스트`() {
        val document = document {
            and(
                { field(Author::age) between (18 to 30) },
            )
        }

    }

    @Test
    fun `between inclusive 연산 테스트`() {
        val document = document {
            and(
                { field(Author::age) betweenInclusive (18 to 30) },
            )
        }

    }

    @Test
    fun `greater than and less than or equal 연산 테스트`() {
        val document = document {
            and(
                { field(Author::age) gt 18 },
                { field(Author::age) lte 30 },
            )
        }

    }

    @Test
    fun `greater than or equal and less than 연산 테스트`() {
        val document = document {
            and(
                { field(Author::age) gte 18 },
                { field(Author::age) lt 30 },
            )
        }

    }

    @Test
    fun `in 연산 테스트`() {
        val document = document {
            and(
                { field(Author::age) `in` listOf(18, 19, 20) },
            )
        }

    }

    @Test
    fun `not in 연산 테스트`() {
        val document = document {
            and(
                { field(Author::age) notIn listOf(18, 19, 20) },
            )
        }

    }

    @Test
    fun `contains 연산 테스트`() {
        val document = document {
            and(
                { field(Author::name) contains "John" },
            )
        }

    }

    @Test
    fun `contains not 연산 테스트`() {
        val document = document {
            and(
                { field(Author::name) containsNot "John" },
            )
        }

    }

    @Test
    fun `starts with 연산 테스트`() {
        val document = document {
            and(
                { field(Author::name) startsWith "John" },
            )
        }

    }

    @Test
    fun `ends with 연산 테스트`() {
        val document = document {
            and(
                { field(Author::name) endsWith "John" },
            )
        }

    }

    @Test
    fun `match 연산 테스트`() {
        val document = document {
            and(
                { field(Author::name) match "John" },
            )
        }

    }

    @Test
    fun `or 연산 테스트`() {
        val document = document {
            or(
                {
                    and(
                        { field(Author::name) eq "John" },
                        { field(Author::age) eq 18 },
                    )
                },
                {
                    and(
                        { field(Author::name) eq "Any" },
                        { field(Author::age) eq 81 },
                    )
                },
            )
        }

    }
}