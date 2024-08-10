package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.QOldAuthor
import com.example.youthconmongodsl.collection.Status
import com.example.youthconmongodsl.collection.YoungAuthor
import com.example.youthconmongodsl.dto.Needs
import com.example.youthconmongodsl.extension.document
import com.example.youthconmongodsl.extension.field
import com.example.youthconmongodsl.extension.find
import com.example.youthconmongodsl.extension.orderBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate

@SpringBootTest
class BeforeQueryDslTest(
    @Autowired private val mongoTemplate: MongoTemplate,
    @Autowired private val queryDSLRepository: AuthorRepositoryQueryDSL,
) {

    @Test
    fun `QueryDSL로 or 조건 내부에서 and 조건으로 묶어 조회한다`() {
        val oldAuthor = QOldAuthor.oldAuthor
        val predicate =
            oldAuthor
                .name.eq("John")
                .and(
                    oldAuthor.age.eq(18)
                )
                .or(
                    oldAuthor.name.eq("Any")
                        .and(
                            oldAuthor.age.eq(81)
                        )
                )

        val firstOrder = oldAuthor.age.desc()
        val secondOrder = oldAuthor.name.asc()

        queryDSLRepository.findAll(predicate, firstOrder, secondOrder)
    }

    @Test
    fun `CustomDSL로 or 조건 내부에서 and 조건으로 묶어 조회한다`() {
        val document = document {
            or(
                {
                    and(
                        { field(YoungAuthor::name) eq "John" },
                        { field(YoungAuthor::age) eq 18 },
                    )
                },
                {
                    and(
                        { field(YoungAuthor::name) eq "Any" },
                        { field(YoungAuthor::age) eq 81 },
                    )
                },
            )
        }.orderBy(YoungAuthor::age).desc()
            .orderBy(YoungAuthor::name).asc()

        mongoTemplate.find(document, YoungAuthor::class)
    }

    @Test
    fun `QueryDSL로 동적 쿼리를 조회한다`() {
        val needs = Needs(
            name = "John",
            age = 18,
            status = Status.ACTIVE,
        )

        val oldAuthor = QOldAuthor.oldAuthor
        val predicate = oldAuthor.run {
            status?.eq(needs.status)
            name.eq(needs.name)
            age.eq(needs.age)
            weight.eq(needs.weight)
            height.eq(needs.height)
        }

        println("predicate: $predicate")
    }
}