package com.example.youthconmongodsl.extension

import com.example.youthconmongodsl.clazz.Group
import com.example.youthconmongodsl.clazz.cast
import com.example.youthconmongodsl.clazz.castIfEnum
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.query.BasicQuery
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

fun <T : Any> MongoTemplate.find(
    query: BasicQuery,
    pageable: Pageable,
    entityClass: KClass<T>,
): List<T> =
    find(
        query.limit(pageable.pageSize)
            .skip(pageable.offset)
            .with(pageable.sort),
        entityClass.java,
    )

fun <T : Any> MongoTemplate.find(
    query: BasicQuery,
    entityClass: KClass<T>,
): List<T> = find(query, entityClass.java)

fun <T : Any> MongoTemplate.findAll(
    query: BasicQuery,
    pageable: Pageable,
    entityClass: KClass<T>,
): Page<T> {
    val data = find(
        query.limit(pageable.pageSize)
            .skip(pageable.offset)
            .with(pageable.sort),
        entityClass.java,
    )
    val count = count(query, entityClass.java)
    return PageImpl(data, pageable, count)
}

fun <T : Any> MongoTemplate.count(
    query: BasicQuery,
    entityClass: KClass<T>,
): Long = count(query, entityClass.java)

inline fun <T : Any, reified K : Any> MongoTemplate.count(
    group: Group<T, K>,
    entityClass: KClass<T>,
    alias: String = "count",
): Map<K, Long> {
    val aggregation = group.count()
    return aggregate(aggregation, entityClass.java, Map::class.java)
        .mappedResults.associate { result ->
            val key = castIfEnum<K, T>(result, entityClass)
            val value = result[alias] as Int
            key to value.toLong()
        }
}

inline fun <reified T : Any, reified R : Any> MongoTemplate.sum(
    query: BasicQuery,
    property: KProperty1<T, R>,
    alias: String = "total",
): R {
    val aggregation = query.sumOf(alias) { field(property) }
    return aggregate(aggregation, T::class.java, Map::class.java)
        .uniqueMappedResult?.let { result ->
            result[alias] as? R
                ?: throw TypeCastException("null cannot be cast to non-null type ${property.returnType}")
        }
        ?: throw NoSuchElementException("No element found")
}

inline fun <reified T : Any, reified R : Any, reified C : Any> MongoTemplate.sum(
    query: BasicQuery,
    property: KProperty1<T, R>,
    castType: KClass<C>,
    alias: String = "total",
): C {
    val aggregation = query.sumOf(alias, castType) { field(property) }
    return aggregate(aggregation, T::class.java, Map::class.java)
        .uniqueMappedResult?.let { result ->
            result[alias].cast<C>()
                ?: throw TypeCastException("null cannot be cast to non-null type ${property.returnType}")
        }
        ?: throw NoSuchElementException("No element found")
}

inline fun <reified T : Any, reified R : Any, reified K : Any> MongoTemplate.sum(
    group: Group<T, K>,
    property: KProperty1<T, R>,
    alias: String = "total",
): Map<K, R> {
    val aggregation = group.sumOf(alias) { field(property) }
    return aggregate(aggregation, T::class.java, Map::class.java)
        .mappedResults.associate { result ->
            val key = castIfEnum<K, T>(result, T::class)
            val value = result[alias] as? R
                ?: throw TypeCastException("null cannot be cast to non-null type ${property.returnType}")
            key to value
        }
}

inline fun <reified T : Any, reified R : Any, reified K : Any, reified C : Any> MongoTemplate.sum(
    group: Group<T, K>,
    property: KProperty1<T, R>,
    castType: KClass<C>,
    alias: String = "total",
): Map<K, C> {
    val aggregation = group.sumOf(alias, castType) { field(property) }
    return aggregate(aggregation, T::class.java, Map::class.java)
        .mappedResults.associate { result ->
            val key = castIfEnum<K, T>(result, T::class)
            val value = result[alias].cast<C>()
                ?: throw TypeCastException("null cannot be cast to non-null type ${property.returnType}")
            key to value
        }
}

inline fun <reified T : Any, reified R : Any> MongoTemplate.avg(
    basicQuery: BasicQuery,
    property: KProperty1<T, R>,
    alias: String = "avg",
): R {
    val aggregation = basicQuery.avgOf(alias) { field(property) }
    return aggregate(aggregation, T::class.java, Map::class.java)
        .uniqueMappedResult?.let { result ->
            val doubleValue = result[alias] as Double
            doubleValue.cast<R>()
        }
        ?: throw NoSuchElementException("No element found")
}

inline fun <reified T : Any, reified R : Any, reified C : Any> MongoTemplate.avg(
    basicQuery: BasicQuery,
    property: KProperty1<T, R>,
    castType: KClass<C>,
    alias: String = "avg",
): C {
    val aggregation = basicQuery.avgOf(alias, castType) { field(property) }
    return aggregate(aggregation, T::class.java, Map::class.java)
        .uniqueMappedResult?.let { result ->
            val doubleValue = result[alias] as Double
            doubleValue.cast<C>()
        }
        ?: throw NoSuchElementException("No element found")
}

inline fun <reified T : Any, reified R : Any, reified K : Any> MongoTemplate.avg(
    group: Group<T, K>,
    property: KProperty1<T, R>,
    alias: String = "avg",
): Map<K, R> {
    val aggregation = group.avgOf(alias) { field(property) }
    return aggregate(aggregation, T::class.java, Map::class.java)
        .mappedResults.associate { result ->
            val key = castIfEnum<K, T>(result, T::class)
            val doubleValue = result[alias] as Double
            val value = doubleValue.cast<R>()
            key to value
        }
}

inline fun <reified T : Any, reified R : Any, reified K : Any, reified C : Any> MongoTemplate.avg(
    group: Group<T, K>,
    property: KProperty1<T, R>,
    castType: KClass<C>,
    alias: String = "avg",
): Map<K, C> {
    val aggregation = group.avgOf(alias, castType) { field(property) }
    return aggregate(aggregation, T::class.java, Map::class.java)
        .mappedResults.associate { result ->
            val key = castIfEnum<K, T>(result, T::class)
            val doubleValue = result[alias] as Double
            val value = doubleValue.cast<C>()
            key to value
        }
}

inline fun <reified T : Any, reified R : Any> MongoTemplate.max(
    basicQuery: BasicQuery,
    property: KProperty1<T, R>,
    alias: String = "max",
): R {
    val aggregation = basicQuery.maxOf(alias) { field(property) }
    return aggregate(aggregation, T::class.java, Map::class.java)
        .uniqueMappedResult?.let { result ->
            result[alias] as? R
                ?: throw TypeCastException("null cannot be cast to non-null type ${property.returnType}")
        }
        ?: throw NoSuchElementException("No element found")
}

inline fun <reified T : Any, reified R : Any, reified C : Any> MongoTemplate.max(
    basicQuery: BasicQuery,
    property: KProperty1<T, R>,
    castType: KClass<C>,
    alias: String = "max",
): C {
    val aggregation = basicQuery.maxOf(alias, castType) { field(property) }
    return aggregate(aggregation, T::class.java, Map::class.java)
        .uniqueMappedResult?.let { result ->
            result[alias].cast<C>()
                ?: throw TypeCastException("null cannot be cast to non-null type ${property.returnType}")
        }
        ?: throw NoSuchElementException("No element found")
}

inline fun <reified T : Any, reified R : Any, reified K : Any> MongoTemplate.max(
    group: Group<T, K>,
    property: KProperty1<T, R>,
    alias: String = "max",
): Map<K, R> {
    val aggregation = group.maxOf(alias) { field(property) }
    return aggregate(aggregation, T::class.java, Map::class.java)
        .mappedResults.associate { result ->
            val key = castIfEnum<K, T>(result, T::class)
            val value = result[alias] as? R
                ?: throw TypeCastException("null cannot be cast to non-null type ${property.returnType}")
            key to value
        }
}

inline fun <reified T : Any, reified R : Any, reified K : Any, reified C : Any> MongoTemplate.max(
    group: Group<T, K>,
    property: KProperty1<T, R>,
    castType: KClass<C>,
    alias: String = "max",
): Map<K, C> {
    val aggregation = group.maxOf(alias, castType) { field(property) }
    return aggregate(aggregation, T::class.java, Map::class.java)
        .mappedResults.associate { result ->
            val key = castIfEnum<K, T>(result, T::class)
            val value = result[alias].cast<C>()
                ?: throw TypeCastException("null cannot be cast to non-null type ${property.returnType}")
            key to value
        }
}

inline fun <reified T : Any, reified R : Any> MongoTemplate.min(
    basicQuery: BasicQuery,
    property: KProperty1<T, R>,
    alias: String = "min",
): R {
    val aggregation = basicQuery.minOf(alias) { field(property) }
    return aggregate(aggregation, T::class.java, Map::class.java)
        .uniqueMappedResult?.let { result ->
            result[alias] as? R
                ?: throw TypeCastException("null cannot be cast to non-null type ${property.returnType}")
        }
        ?: throw NoSuchElementException("No element found")
}

inline fun <reified T : Any, reified R : Any, reified C : Any> MongoTemplate.min(
    basicQuery: BasicQuery,
    property: KProperty1<T, R>,
    castType: KClass<C>,
    alias: String = "min",
): C {
    val aggregation = basicQuery.minOf(alias, castType) { field(property) }
    return aggregate(aggregation, T::class.java, Map::class.java)
        .uniqueMappedResult?.let { result ->
            result[alias].cast<C>()
                ?: throw TypeCastException("null cannot be cast to non-null type ${property.returnType}")
        }
        ?: throw NoSuchElementException("No element found")
}

inline fun <reified T : Any, reified R : Any, reified K : Any> MongoTemplate.min(
    group: Group<T, K>,
    property: KProperty1<T, R>,
    alias: String = "min",
): Map<K, R> {
    val aggregation = group.minOf(alias) { field(property) }
    return aggregate(aggregation, T::class.java, Map::class.java)
        .mappedResults.associate { result ->
            val key = castIfEnum<K, T>(result, T::class)
            val value = result[alias] as? R
                ?: throw TypeCastException("null cannot be cast to non-null type ${property.returnType}")
            key to value
        }
}

inline fun <reified T : Any, reified R : Any, reified K : Any, reified C : Any> MongoTemplate.min(
    group: Group<T, K>,
    property: KProperty1<T, R>,
    castType: KClass<C>,
    alias: String = "min",
): Map<K, C> {
    val aggregation = group.minOf(alias, castType) { field(property) }
    return aggregate(aggregation, T::class.java, Map::class.java)
        .mappedResults.associate { result ->
            val key = castIfEnum<K, T>(result, T::class)
            val value = result[alias].cast<C>()
                ?: throw TypeCastException("null cannot be cast to non-null type ${property.returnType}")
            key to value
        }
}

val KClass<*>.fieldName
    get() = this.java.declaredFields.first { it.isAnnotationPresent(Id::class.java) }
        ?.run {
            isAccessible = true
            val hasFieldAnnotation = annotations.any { it is Field }
            if (hasFieldAnnotation) annotations.filterIsInstance<Field>().first().value
            else "_id"
        }
        ?: this.simpleName!!