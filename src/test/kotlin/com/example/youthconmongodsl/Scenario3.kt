package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.collection.Status
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mapping.toDotPath
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.GroupOperation
import org.springframework.data.mongodb.core.aggregation.MatchOperation
import org.springframework.data.mongodb.core.query.Criteria
import kotlin.reflect.KProperty

@SpringBootTest
@Suppress("SuspiciousCallableReferenceInLambda")
class Scenario3(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    @Test
    @DisplayName("대소문자 구분 없이 별명에 hy가 포함되고 나이가 20대인 작가를 상태별로 그루핑한 뒤 가진 돈의 합을 구한다")
    fun asIs() {
        val aggregation = where {
            Author::nickname containsIgnoreCase "hy"
            Author::age between 20..29
        } groupBy {
            Author::status
        } sum {
            Author::money alias TOTAL_MONEY
        }



        // 조회
        val aggregationResults = mongoTemplate.aggregate(aggregation, Author::class.java, Map::class.java)

        // 검증
        val statusToAverageMoney = aggregationResults.mappedResults.associate {
            val status = Status.valueOf(it[ID].toString())
            val total = it[TOTAL_MONEY] as Long
            status to total
        }

        assertThat(statusToAverageMoney[Status.ACTIVE]).isEqualTo(81428347)
        assertThat(statusToAverageMoney[Status.REST]).isEqualTo(176777263)
        assertThat(statusToAverageMoney[Status.RETIREMENT]).isEqualTo(181507261)
    }

    companion object {
        private const val ID = "_id"
        private const val TOTAL_MONEY = "totalMoney"
    }
}

class Group(
    private val criteria: Criteria,
    private val groupProperty: KProperty<*>,
) {
    class Sum(
        private val groupOperation: GroupOperation,
    ) {
        infix fun KProperty<*>.alias(
            value: String,
        ): GroupOperation {
            return groupOperation.sum(this.toDotPath()).`as`(value)
        }
    }

    infix fun sum(
        block: Sum.() -> GroupOperation,
    ): Aggregation {
        val matchOperation = MatchOperation(criteria)
        val groupOperation = Sum(Aggregation.group(groupProperty.toDotPath())).block()
        return Aggregation.newAggregation(matchOperation, groupOperation)
    }
}

infix fun Criteria.groupBy(
    block: () -> KProperty<*>,
): Group {
    return Group(this, block.invoke())
}