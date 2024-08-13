package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.collection.Status.*
import com.example.youthconmongodsl.extension.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate

@SpringBootTest
class GroupTest(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    @BeforeEach
    fun setUp() {
        mongoTemplate.dropCollection(Author::class.java)
        mongoTemplate.insertAll(
            listOf(
                Author.of(
                    name = "John",
                    age = 10,
                    status = REST,
                    books = mutableListOf(),
                ),
                Author.of(
                    name = "John",
                    age = 20,
                    status = ACTIVE,
                    books = mutableListOf(),
                ),
                Author.of(
                    name = "John",
                    age = 30,
                    status = ACTIVE,
                    books = mutableListOf(),
                ),
                Author.of(
                    name = "John",
                    age = 40,
                    status = ACTIVE,
                    books = mutableListOf(),
                ),
            )
        )
    }
    @Test
    fun `전체에 대한 count 를 구한다`() {
        val document = document {
            and(
                { field(Author::name) eq "John" },
            )
        }

        val count = mongoTemplate.count(document, Author::class)
        assert(count == 4L)
    }

    @Test
    fun `grouping 된 count 를 구한다`() {
        val document = document {
            and(
                { field(Author::name) eq "John" },
            )
        }

        val statusGroup = document.groupBy(Author::status)
        val countOfGroup = mongoTemplate.count(statusGroup, Author::class)
        assert(countOfGroup == mapOf(ACTIVE to 3L, REST to 1L))
    }

    @Test
    fun `전체에 대한 합을 구한다`() {
        val document = document {
            and(
                { field(Author::name) eq "John" },
            )
        }

        val sumOfAge = mongoTemplate.sum(document, Author::age)
        assert(sumOfAge == 100)
    }

    @Test
    fun `grouping 된 필드에 대한 합을 구한다`() {
        val document = document {
            and(
                { field(Author::name) eq "John" },
            )
        }

        val statusGroup = document.groupBy(Author::status)
        val sumOfGroup = mongoTemplate.sum(statusGroup, Author::age)
        assert(sumOfGroup == mapOf(ACTIVE to 90, REST to 10))
    }

    @Test
    fun `전체에 대한 평균을 구한다`() {
        val document = document {
            and(
                { field(Author::name) eq "John" },
            )
        }

        val avgOfAge = mongoTemplate.avg(document, Author::age)
        assert(avgOfAge == 25)
    }

    @Test
    fun `grouping 된 평균을 구한다`() {
        val document = document {
            and(
                { field(Author::name) eq "John" },
            )
        }

        val statusGroup = document.groupBy(Author::status)
        val avgOfAge = mongoTemplate.avg(statusGroup, Author::age)
        assert(avgOfAge == mapOf(ACTIVE to 30, REST to 10))
    }

    @Test
    fun `전체에 대한 최대값을 구한다`() {
        val document = document {
            and(
                { field(Author::name) eq "John" },
            )
        }

        val maxOfAge = mongoTemplate.max(document, Author::age)
        assert(maxOfAge == 40)
    }

    @Test
    fun `grouping 된 최대값을 구한다`() {
        val document = document {
            and(
                { field(Author::name) eq "John" },
            )
        }

        val nameGroup = document.groupBy(Author::name)
        val maxOfAge = mongoTemplate.max(nameGroup, Author::age)
        assert(maxOfAge == mapOf("John" to 40))
    }

    @Test
    fun `전체에 대한 최소값을 구한다`() {
        val document = document {
            and(
                { field(Author::name) eq "John" },
            )
        }

        val minOfAge = mongoTemplate.min(document, Author::age)
        assert(minOfAge == 10)
    }

    @Test
    fun `grouping 된 최소값을 구한다`() {
        val document = document {
            and(
                { field(Author::name) eq "John" },
            )
        }

        val nameGroup = document.groupBy(Author::name)
        val minOfAge = mongoTemplate.min(nameGroup, Author::age)
        assert(minOfAge == mapOf("John" to 10))
    }
}