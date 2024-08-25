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
    @DisplayName("나이가 20대이고 책을 3권 낸 작가를 상태별로 그루핑한 뒤 가진 돈의 합을 구한다 as is")
    fun asIs() {
        val criteria = Criteria().andOperator(
            Criteria.where("age").gte(20).lte(29),
            Criteria.where("books").size(3),
        )

        val matchOperation = MatchOperation(criteria)
        val groupOperation = Aggregation.group("status").sum("money").`as`(TOTAL_MONEY)
        val aggregation = Aggregation.newAggregation(matchOperation, groupOperation)



        // 조회
        val aggregationResults = mongoTemplate.aggregate(aggregation, Author::class.java, Map::class.java)

        // 검증
        val statusToAverageMoney = aggregationResults.mappedResults.associate {
            val status = Status.valueOf(it[ID].toString())
            val total = it[TOTAL_MONEY] as Long
            status to total
        }

        assertThat(statusToAverageMoney[Status.ACTIVE]).isEqualTo(2161328487)
        assertThat(statusToAverageMoney[Status.REST]).isEqualTo(2110033461)
        assertThat(statusToAverageMoney[Status.RETIREMENT]).isEqualTo(2486962006)
    }

    companion object {
        private const val ID = "_id"
        private const val TOTAL_MONEY = "totalMoney"
    }
}