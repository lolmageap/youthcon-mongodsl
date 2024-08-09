package com.example.youthconmongodsl.clazz

import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.MatchOperation
import org.springframework.data.mongodb.core.query.Criteria
import kotlin.reflect.KClass

class EmptyGroup(
    private val document: Document,
) {
    fun sumOf(
        type: KClass<*>? = null,
        alias: String = "total",
        sumField: Document.() -> Field<*, *>,
    ): Aggregation {
        val fieldName = sumField.invoke(Document()).key.name
        return if (type == null) {
            val matchStage = document.matchOperation()
            Aggregation.newAggregation(
                matchStage,
                Aggregation.group().sum("\$$fieldName").`as`(alias),
            )
        } else {
            val expression = AggregationExpression {
                Document(MongoTypeCaster.cast(type), "\$$fieldName")
            }
            val matchStage = document.matchOperation()
            Aggregation.newAggregation(
                matchStage,
                Aggregation.group().sum(expression).`as`(alias),
            )
        }
    }

    fun avgOf(
        type: KClass<*>? = null,
        alias: String = "avg",
        avgField: Document.() -> Field<*, *>,
    ): Aggregation {
        val fieldName = avgField.invoke(Document()).key.name

        return if (type == null) {
            val matchStage = document.matchOperation()
            Aggregation.newAggregation(
                matchStage,
                Aggregation.group().avg("\$$fieldName").`as`(alias),
            )
        } else {
            val expression = AggregationExpression {
                Document(MongoTypeCaster.cast(type), "\$$fieldName")
            }
            val matchStage = document.matchOperation()
            Aggregation.newAggregation(
                matchStage,
                Aggregation.group().avg(expression).`as`(alias),
            )
        }
    }

    fun maxOf(
        type: KClass<*>? = null,
        alias: String = "max",
        maxField: Document.() -> Field<*, *>,
    ): Aggregation {
        val fieldName = maxField.invoke(Document()).key.name
        return if (type == null) {
            val matchStage = document.matchOperation()
            Aggregation.newAggregation(
                matchStage,
                Aggregation.group().max("\$$fieldName").`as`(alias),
            )
        } else {
            val expression = AggregationExpression {
                Document(MongoTypeCaster.cast(type), "\$$fieldName")
            }
            val matchStage = document.matchOperation()
            Aggregation.newAggregation(
                matchStage,
                Aggregation.group().max(expression).`as`(alias),
            )
        }
    }

    fun minOf(
        type: KClass<*>? = null,
        alias: String = "min",
        minField: Document.() -> Field<*, *>,
    ): Aggregation {
        val fieldName = minField.invoke(Document()).key.name
        return if (type == null) {
            val matchStage = document.matchOperation()
            Aggregation.newAggregation(
                matchStage,
                Aggregation.group().min("\$$fieldName").`as`(alias),
            )
        } else {
            val expression = AggregationExpression {
                Document(MongoTypeCaster.cast(type), "\$$fieldName")
            }
            val matchStage = document.matchOperation()
            Aggregation.newAggregation(
                matchStage,
                Aggregation.group().min(expression).`as`(alias),
            )
        }
    }

    fun count(
        alias: String = "count",
    ): Aggregation {
        val matchStage = document.matchOperation()
        return Aggregation.newAggregation(
            matchStage,
            Aggregation.group().count().`as`(alias),
        )
    }

    private fun Document.matchOperation(): MatchOperation {
        val criteria = Criteria()
        for ((key, value) in this) {
            criteria.and(key).`is`(value)
        }
        return Aggregation.match(criteria)
    }
}