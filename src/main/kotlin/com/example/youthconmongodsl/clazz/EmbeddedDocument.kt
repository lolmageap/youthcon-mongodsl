package com.example.youthconmongodsl.clazz

import kotlin.reflect.KProperty1

class EmbeddedDocument private constructor(
    private val property: KProperty1<*, *>,
) {
    val name: String = property.name

    companion object {
        fun of(
            property: KProperty1<*, *>,
        ) = EmbeddedDocument(property)
    }
}

fun embeddedDocument(
    property: KProperty1<*, *>,
) = EmbeddedDocument.of(property)
