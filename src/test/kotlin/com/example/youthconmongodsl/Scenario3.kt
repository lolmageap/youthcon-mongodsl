package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.MatchOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.where

@SpringBootTest
class Scenario3(
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
    }

    @Test
    fun `책을 3권 이상 낸 30대 이상의 작가의 돈의 총합을 구한다 to be`() {
        val criteria = andOperator(
            where(Author::books).size(3),
            where(Author::age).gte(30),
        )
    }
}