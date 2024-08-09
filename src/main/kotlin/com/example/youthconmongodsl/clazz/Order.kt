package com.example.youthconmongodsl.clazz

import com.example.youthconmongodsl.extension.copy
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.query.BasicQuery
import kotlin.reflect.KProperty1

data class Order(
    val basicQuery: BasicQuery,
    val key: KProperty1<*, *>,
) {
    fun asc() = basicQuery.sorting(Sort.Direction.ASC)
    fun desc() = basicQuery.sorting(Sort.Direction.DESC)

    private fun BasicQuery.sorting(
        direction: Sort.Direction,
    ): BasicQuery {
        val document = this.queryObject.copy()
        val existingSort = this.extractSortObject()
        val newSort = Sort.by(direction, key.name)
        val combinedSort = existingSort.and(newSort)
        val newBasicQuery = BasicQuery(document)
        newBasicQuery.with(combinedSort)
        return newBasicQuery
    }

    private fun BasicQuery.extractSortObject() =
        Sort.by(
            this.sortObject.entries.map {
                val sort = when (it.value) {
                    ASC -> Sort.Direction.ASC
                    else -> Sort.Direction.DESC
                }
                Sort.Order(sort, it.key)
            }
        )

    companion object {
        private const val DESC = "-1"
        private const val ASC = "1"
    }
}