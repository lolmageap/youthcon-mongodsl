package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.YoungAuthor
import com.example.youthconmongodsl.collection.Status.ACTIVE
import com.example.youthconmongodsl.collection.Status.INACTIVE
import com.example.youthconmongodsl.extension.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import java.math.BigDecimal

@SpringBootTest
class GroupTest(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    @Test
    fun `전체에 대한 count 를 구한다`() {
        val document = document {
            and(
                { field(YoungAuthor::name) eq "John" },
            )
        }

        val count = mongoTemplate.count(document, YoungAuthor::class)
        assert(count == 4L)
    }

    @Test
    fun `grouping 된 count 를 구한다`() {
        val document = document {
            and(
                { field(YoungAuthor::name) eq "John" },
            )
        }

        val statusGroup = document.groupBy(YoungAuthor::status)
        val countOfGroup = mongoTemplate.count(statusGroup, YoungAuthor::class)
        assert(countOfGroup == mapOf(ACTIVE to 3, INACTIVE to 1))
    }

    @Test
    fun `전체에 대한 합을 구한다`() {
        val document = document {
            and(
                { field(YoungAuthor::name) eq "John" },
            )
        }

        val sumOfAge = mongoTemplate.sum(document, YoungAuthor::age)
        assert(sumOfAge == 100)
    }

    @Test
    fun `grouping 된 필드에 대한 합을 구한다`() {
        val document = document {
            and(
                { field(YoungAuthor::name) eq "John" },
            )
        }

        val statusGroup = document.groupBy(YoungAuthor::status)
        val sumOfGroup = mongoTemplate.sum(statusGroup, YoungAuthor::age)
        assert(sumOfGroup == mapOf(ACTIVE to 90, INACTIVE to 10))
    }

    @Test
    fun `전체에 대한 평균을 구한다`() {
        val document = document {
            and(
                { field(YoungAuthor::name) eq "John" },
            )
        }

        val avgOfAge = mongoTemplate.avg(document, YoungAuthor::age)
        assert(avgOfAge == 25)
    }

    @Test
    fun `grouping 된 평균을 구한다`() {
        val document = document {
            and(
                { field(YoungAuthor::name) eq "John" },
            )
        }

        val statusGroup = document.groupBy(YoungAuthor::status)
        val avgOfAge = mongoTemplate.avg(statusGroup, YoungAuthor::age)
        assert(avgOfAge == mapOf(ACTIVE to 30, INACTIVE to 10))
    }

    @Test
    fun `전체에 대한 최대값을 구한다`() {
        val document = document {
            and(
                { field(YoungAuthor::name) eq "John" },
            )
        }

        val maxOfAge = mongoTemplate.max(document, YoungAuthor::age)
        assert(maxOfAge == 40)
    }

    @Test
    fun `grouping 된 최대값을 구한다`() {
        val document = document {
            and(
                { field(YoungAuthor::name) eq "John" },
            )
        }

        val nameGroup = document.groupBy(YoungAuthor::name)
        val maxOfAge = mongoTemplate.max(nameGroup, YoungAuthor::age)
        assert(maxOfAge == mapOf("John" to 40))
    }

    @Test
    fun `전체에 대한 최소값을 구한다`() {
        val document = document {
            and(
                { field(YoungAuthor::name) eq "John" },
            )
        }

        val minOfAge = mongoTemplate.min(document, YoungAuthor::age)
        assert(minOfAge == 10)
    }

    @Test
    fun `grouping 된 최소값을 구한다`() {
        val document = document {
            and(
                { field(YoungAuthor::name) eq "John" },
            )
        }

        val nameGroup = document.groupBy(YoungAuthor::name)
        val minOfAge = mongoTemplate.min(nameGroup, YoungAuthor::age)
        assert(minOfAge == mapOf("John" to 10))
    }

    @Test
    fun `전체에 대한 데이터를 다른 타입으로 컨버팅 하고 연산을 할 수 있다`() {
        val document = document {
            and(
                { field(YoungAuthor::name) eq "John" },
            )
        }

        val totalHeight = mongoTemplate.sum(document, YoungAuthor::height, BigDecimal::class)
        assert(totalHeight.roundOff == 740.toBigDecimal())
    }
}

private val BigDecimal.roundOff: BigDecimal
    get() = this.setScale(0)