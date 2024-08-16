package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Direction.*
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.and
import org.springframework.data.mongodb.core.query.where
import kotlin.reflect.KProperty

@SpringBootTest
class Scenario2(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    @Test
    fun `별명이 존재하지 않는 작가 또는 나이가 50 이상이고 키가 160cm 이하인 작가를 나이가 많은 순으로 조회한다 as is`() {
        val criteria = Criteria().orOperator(
            where("nickname").exists(false),
            where("age").gte(50).and("height").lte(160),
        )

        val query = Query.query(criteria).with(Sort.by(Sort.Order.desc("age")))
        val authors = mongoTemplate.find(query, Author::class.java)
    }

    @Test
    fun `별명이 존재하지 않는 작가 또는 나이가 50 이상이고 키가 160cm 이하인 작가를 나이가 많은 순으로 조회한다 to be`() {
        val criteria = orOperator(
            where(Author::nickname) exists false,
            where(Author::age) gte 50 and Author::height lte 160,
        ).toQuery()
            .orderBy(Author::age, DESC)

        val authors = mongoTemplate.find(criteria, Author::class)
    }

    /**
     * 1. 첫번째 시나리오에서 andOperator를 만들었던 것 처럼 orOperator를 만들어서 Criteria를 생성해도 될것 습니다.
     *
     * 2. where(key).exists(false) <- 이러한 부분을 코틀린의 infix 함수를 사용하여 변경할 수 있을것 같습니다.
     *    infix 함수는 중위 함수라고도 불리며, 함수를 호출할때 .을 생략할 수 있습니다.
     *    하지만 infix 함수를 사용한다고 무조건 가독성이 좋아지는것은 아닙니다.
     *    복잡한 연산을 할 때 gte 함수와 and 함수, lte 함수 모두 infix 함수로 만들면 가독성이 떨어질 수 있습니다.
     *    한번 직접 코드로 작성해보고 가독성을 확인해보시죠!
     *
     * 3. 정렬(sort)을 할때도 with(Sort.by(Sort.Order.asc("age"))) 이 부분도 더 깔끔하게 변경할 수 있을것 같습니다.
     *    우리는 조금 더 명시적으로 orderBy 라는 키워드를 사용해서 함수를 한번 만들어보겠습니다.
     */
}

fun orOperator(
    vararg criteria: Criteria,
) =
    Criteria().orOperator(*criteria)

infix fun Criteria.exists(
    value: Boolean,
) =
    this.exists(value)

fun Query.orderBy(
    key: KProperty<*>,
    direction: Direction,
) =
    this.with(
        Sort.by(
            Sort.Order(direction, key.name)
        )
    )

infix fun Criteria.gte(
    value: Any,
) = this.gte(value)

infix fun Criteria.lte(
    value: Any,
) = this.lte(value)