package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.collection.Status
import com.example.youthconmongodsl.extension.document
import com.example.youthconmongodsl.extension.field
import com.example.youthconmongodsl.extension.groupBy
import com.example.youthconmongodsl.extension.sum
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.MatchOperation
import org.springframework.data.mongodb.core.query.Criteria

@SpringBootTest
class Scenario4(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    @Test
    fun `책을 3권 이상 낸 30대 이상의 작가의 돈의 총합을 구한다 as is`() {
        val criteria = Criteria().andOperator(
            Criteria.where("books").size(3),
            Criteria.where("age").gte(30),
        )

        val matchOperation = MatchOperation(criteria)
        val groupOperation = Aggregation.group().sum("money").`as`("totalMoney")

        val aggregation = Aggregation.newAggregation(matchOperation, groupOperation)
        val aggregationResults = mongoTemplate.aggregate(aggregation, Author::class.java, Map::class.java)

        val totalMoney = aggregationResults.mappedResults.first()["totalMoney"] as Long
        // TODO : assert total money
    }

    @Test
    fun `책을 3권 이상 낸 30대 이상의 작가의 돈의 총합을 구한다 to be`() {
        val document = document {
            and(
                { field(Author::books) size 3 },
                { field(Author::age) gte 30 },
            )
        }

        val totalMoney = mongoTemplate.sum(document, Author::money)
        // TODO : assert total money
    }

    @Test
    fun `책을 3권 이상 낸 30대 이상의 작가를 상태 별로 그루핑하여 돈의 합을 구한다 as is`() {
        val criteria = Criteria().andOperator(
            Criteria.where("books").size(3),
            Criteria.where("age").gte(30),
        )

        val matchOperation = MatchOperation(criteria)
        val groupOperation = Aggregation.group("status").sum("money").`as`("totalMoney")

        val aggregation = Aggregation.newAggregation(matchOperation, groupOperation)
        val aggregationResults = mongoTemplate.aggregate(aggregation, Author::class.java, Map::class.java)

        val statusToTotalMoney = aggregationResults.mappedResults.associate {
            it["_id"] as Status to it["totalMoney"] as Long
        }
        // TODO : assert total money
    }

    @Test
    fun `책을 3권 이상 낸 30대 이상의 작가를 상태 별로 그루핑하여 돈의 합을 구한다 to be`() {
        val group = document {
            and(
                { field(Author::books) size 3 },
                { field(Author::age) gte 30 },
            )
        }.groupBy(Author::status)

        val statusToTotalMoney = mongoTemplate.sum(group, Author::money)
        // TODO : assert total money
    }
}