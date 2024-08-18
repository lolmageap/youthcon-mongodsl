package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.collection.Status
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Direction.*
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.aggregation.GroupOperation
import org.springframework.data.mongodb.core.aggregation.MatchOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.and
import org.springframework.data.mongodb.core.query.where
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 *    이번에는 통계 쿼리를 작성해보겠습니다.
 *    일반적으로 RDBMS에서는 GROUP BY를 사용하여 통계 쿼리를 작성합니다.
 *    MongoDB에서는 Aggregation Framework를 사용하여 통계 쿼리를 작성합니다.
 *    현재 작성된 코드에 대해서 간단하게 설명하겠습니다.
 *
 *    1. MatchOperation을 사용하여 나이가 20대인 키가 160cm 이상인 작가를 필터링합니다.
 *    2. GroupOperation을 사용하여 상태별로 그루핑한 뒤 가진 돈의 합을 구합니다.
 *    3. MatchOperation 과 GroupOperation을 하나의 Aggregation으로 묶어서 실행합니다.
 *    4. 결과를 Map으로 받아와서 상태별로 가진 돈의 합을 구합니다.
 *
 *    MongoDB를 모르는 사람은 이 코드를 이해하기 어려울 수 있습니다.
 *    그래서 우리는 일반적인 RDBMS의 Group By 쿼리처럼 만들어보려고 합니다.
 *    이번에는 Criteria를 확장함수를 사용하여 Group By 쿼리를 작성해보겠습니다.
 *
 *    어떤 필드를 그루핑 할 건지 알려주는 groupBy 함수와 그루핑한 뒤 합계를 구하는 sum 함수를 만들어보겠습니다.
 *    그리고 이러한 조건들을 만족 시킬 수 있는 Group이라는 클래스도 별도로 만들어주겠습니다.
 *    이렇게 하면 MongoDB의 Aggregation Framework를 사용하지 않고도 Group By 쿼리를 작성할 수 있습니다.
 */
@SpringBootTest
class Scenario3(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    @Test
    fun `나이가 20대이고 키가 160cm 이상인 작가를 상태별로 그루핑한 뒤 가진 돈의 합을 구한다 as is`() {
        val criteria = Criteria().andOperator(
            Criteria.where("age").gte(20).lte(29),
            Criteria.where("height").gte(160),
        )

        val matchOperation = MatchOperation(criteria)
        val groupOperation = Aggregation.group("status").sum("money").`as`("totalMoney")
        val aggregation = Aggregation.newAggregation(matchOperation, groupOperation)

        val aggregationResults = mongoTemplate.aggregate(aggregation, Author::class.java, Map::class.java)


        // 검증
        val statusToAverageMoney = aggregationResults.mappedResults.associate {
            val status = Status.valueOf(it["_id"].toString())
            val total = it["totalMoney"] as Long
            status to total
        }

        Assertions.assertThat(statusToAverageMoney[Status.ACTIVE]).isEqualTo(7264569615)
        Assertions.assertThat(statusToAverageMoney[Status.REST]).isEqualTo(7327825153)
        Assertions.assertThat(statusToAverageMoney[Status.RETIREMENT]).isEqualTo(7566660567)
    }

    @Test
    fun `나이가 20대이고 키가 160cm 이상인 작가를 상태별로 그루핑한 뒤 가진 돈의 합을 구한다 to be`() {
        val aggregation = andOperator(
            where(Author::age) gte 20 lte 29,
            where(Author::height) gte 160,
        ).groupBy(Author::status)
            .sum(Author::money, "totalMoney")

        val aggregationResults = mongoTemplate.aggregate(aggregation, Author::class.java, Map::class.java)


        // 검증
        val statusToAverageMoney = aggregationResults.mappedResults.associate {
            val status = Status.valueOf(it["_id"].toString())
            val total = it["totalMoney"] as Long
            status to total
        }

        Assertions.assertThat(statusToAverageMoney[Status.ACTIVE]).isEqualTo(7264569615)
        Assertions.assertThat(statusToAverageMoney[Status.REST]).isEqualTo(7327825153)
        Assertions.assertThat(statusToAverageMoney[Status.RETIREMENT]).isEqualTo(7566660567)
    }
}

fun Criteria.groupBy(
    property: KProperty<*>,
) = Group(this, property)

fun GroupOperation.sum(
    property: KProperty<*>,
) = this.sum(property.name)

infix fun Criteria.lte(
    value: Any,
) = this.lte(value)

class Group(
    private val criteria: Criteria,
    private val property: KProperty<*>,
) {
    fun sum(
        sumProperty: KProperty<*>,
        alias: String,
    ) =
        Aggregation.newAggregation(
            MatchOperation(criteria),
            Aggregation.group(property.name).sum(sumProperty.name).`as`(alias)
        )
}