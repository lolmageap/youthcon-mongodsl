package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.collection.Status
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.MatchOperation
import org.springframework.data.mongodb.core.query.Criteria

@SpringBootTest
class Scenario3(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    @Test
    fun `나이가 20대이고 키가 160cm 이상인 작가를 상태별로 그루핑한 뒤 가진 돈의 합을 구한다`() {
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
}