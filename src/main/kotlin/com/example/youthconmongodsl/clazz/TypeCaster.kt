package com.example.youthconmongodsl.clazz

import com.example.youthconmongodsl.extension.fieldName
import java.math.BigDecimal
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

@Suppress("UNCHECKED_CAST")
inline fun <reified K : Any, T : Any> castIfEnum(
    result: Map<*, *>,
    entityClass: KClass<T>
) = if (K::class.isSubclassOf(Enum::class)) {
    val enumClass = K::class as KClass<Enum<*>>
    val enumName = result[entityClass.fieldName] as String
    enumClass.java.enumConstants.first { it.name == enumName } as K
} else {
    result[entityClass.fieldName] as K
}

inline fun <reified R : Any> Double.cast() =
    when (R::class) {
        Int::class -> this.toInt() as R
        Long::class -> this.toLong() as R
        Double::class -> this as R
        Float::class -> this.toFloat() as R
        BigDecimal::class -> this as R
        else -> throw IllegalArgumentException("Unsupported type")
    }

inline fun <reified C : Any> Any?.cast() =
    when (C::class) {
        Long::class -> (this as Number).toLong() as? C
        BigDecimal::class -> BigDecimal(this.toString()) as? C
        Int::class -> (this as Number).toInt() as? C
        Double::class -> (this as Number).toDouble() as? C
        Float::class -> (this as Number).toFloat() as? C
        String::class -> this.toString() as? C
        Date::class -> Date(this.toString().toLong()) as? C
        Boolean::class -> this as? C
        else -> throw IllegalArgumentException("Unsupported type: $this")
    }