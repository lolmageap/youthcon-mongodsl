package com.example.youthconmongodsl.clazz

import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.MatchOperation
import org.springframework.data.mongodb.core.query.Criteria
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

/**
 * grouping 을 할때 내부적으로 matchOperation 을 사용합니다.
 * side effect 가 발생하지 않으려면 Group By 는 마지막 줄에 위치 시키는 것이 좋습니다.
 */
class Group<T, R>(
    private val key: KProperty1<T, R>,
    private val document: Document,
) {
    fun sumOf(
        alias: String = "total",
        type: KClass<*>? = null,
        sumField: Document.() -> Field<T, *>,
    ): Aggregation {
        val fieldName = sumField.invoke(Document()).key.name

        return if (type == null) {
            val matchStage = document.matchOperation()
            Aggregation.newAggregation(
                matchStage,
                Aggregation.group(key.name).sum("\$$fieldName").`as`(alias),
            )
        } else {
            val expression = AggregationExpression {
                Document(MongoTypeCaster.cast(type), "\$$fieldName")
            }
            val matchStage = document.matchOperation()
            Aggregation.newAggregation(
                matchStage,
                Aggregation.group(key.name).sum(expression).`as`(alias),
            )
        }
    }

    fun avgOf(
        alias: String = "avg",
        type: KClass<*>? = null,
        avgField: Document.() -> Field<T, *>,
    ): Aggregation {
        val fieldName = avgField.invoke(Document()).key.name

        return if (type == null) {
            val matchStage = document.matchOperation()
            Aggregation.newAggregation(
                matchStage,
                Aggregation.group(key.name).avg("\$$fieldName").`as`(alias),
            )
        } else {
            val expression = AggregationExpression {
                Document(MongoTypeCaster.cast(type), "\$$fieldName")
            }
            val matchStage = document.matchOperation()
            Aggregation.newAggregation(
                matchStage,
                Aggregation.group(key.name).avg(expression).`as`(alias),
            )
        }
    }

    fun maxOf(
        alias: String = "max",
        type: KClass<*>? = null,
        maxField: Document.() -> Field<T, *>,
    ): Aggregation {
        val fieldName = maxField.invoke(Document()).key.name

        return if (type == null) {
            val matchStage = document.matchOperation()
            Aggregation.newAggregation(
                matchStage,
                Aggregation.group(key.name).max("\$$fieldName").`as`(alias),
            )
        } else {
            val expression = AggregationExpression {
                Document(MongoTypeCaster.cast(type), "\$$fieldName")
            }
            val matchStage = document.matchOperation()
            Aggregation.newAggregation(
                matchStage,
                Aggregation.group(key.name).max(expression).`as`(alias),
            )
        }
    }

    fun minOf(
        alias: String = "min",
        type: KClass<*>? = null,
        minField: Document.() -> Field<T, *>,
    ): Aggregation {
        val fieldName = minField.invoke(Document()).key.name

        return if (type == null) {
            val matchStage = document.matchOperation()
            Aggregation.newAggregation(
                matchStage,
                Aggregation.group(key.name).min("\$$fieldName").`as`(alias),
            )
        } else {
            val expression = AggregationExpression {
                Document(MongoTypeCaster.cast(type), "\$$fieldName")
            }
            val matchStage = document.matchOperation()
            Aggregation.newAggregation(
                matchStage,
                Aggregation.group(key.name).min(expression).`as`(alias),
            )
        }
    }

    fun count(
        alias: String = "count",
    ): Aggregation {
        val matchStage = document.matchOperation()
        return Aggregation.newAggregation(
            matchStage,
            Aggregation.group(key.name).count().`as`(alias),
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