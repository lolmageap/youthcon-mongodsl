package com.example.youthconmongodsl.clazz

import java.math.BigDecimal
import java.util.*
import kotlin.reflect.KClass

object MongoTypeCaster {
    fun <T : Any> cast(
        type: KClass<T>,
    ) =
        when (type) {
            Long::class -> "\$toLong"
            BigDecimal::class -> "\$toDecimal"
            Int::class -> "\$toInt"
            Double::class -> "\$toDouble"
            Float::class -> "\$toFloat"
            String::class -> "\$toString"
            Date::class -> "\$toDate"
            Boolean::class -> "\$toBool"
            else -> throw IllegalArgumentException("Unsupported type: $type")
        }
}