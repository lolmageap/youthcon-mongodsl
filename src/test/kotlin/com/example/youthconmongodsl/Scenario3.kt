package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.collection.Status
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.MatchOperation
import org.springframework.data.mongodb.core.query.Criteria

@SpringBootTest
@Suppress("SuspiciousCallableReferenceInLambda")
class Scenario3(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    @Test
    @DisplayName("대소문자 구분 없이 별명에 hy가 포함되고 나이가 20대인 작가를 상태별로 그루핑한 뒤 가진 돈의 합을 구한다")
    fun asIs() {
        val criteria = Criteria().andOperator(
            Criteria.where("nickname").regex("hy", "i"),
            Criteria.where("age").gte(20).lte(29),
        )

        val matchOperation = MatchOperation(criteria)
        val groupOperation = Aggregation.group("status").sum("money").`as`(TOTAL_MONEY)
        val aggregation = Aggregation.newAggregation(matchOperation, groupOperation)


        // 조회
        val aggregationResults = mongoTemplate.aggregate(aggregation, Author::class.java, Map::class.java)

        // 검증
        val statusToTotalMoney = aggregationResults.mappedResults.associate {
            val status = Status.valueOf(it[ID].toString())
            val total = it[TOTAL_MONEY] as Long
            status to total
        }

        assertThat(statusToTotalMoney[Status.ACTIVE]).isEqualTo(81428347)
        assertThat(statusToTotalMoney[Status.REST]).isEqualTo(176777263)
        assertThat(statusToTotalMoney[Status.RETIREMENT]).isEqualTo(181507261)
    }

    companion object {
        private const val ID = "_id"
        private const val TOTAL_MONEY = "totalMoney"
    }
}