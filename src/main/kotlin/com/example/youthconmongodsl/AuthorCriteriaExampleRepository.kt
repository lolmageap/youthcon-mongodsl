package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.YoungAuthor
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.repository.MongoRepository

interface AuthorCriteriaRepository : MongoRepository<YoungAuthor, String>, AuthorRepositoryCustomize

interface AuthorRepositoryRaw {
    fun findAuthors(
        names: List<String>,
        minAge: Int?,
        maxAge: Int?,
        nickname: String?,
    ): List<YoungAuthor>
}

class AuthorRepositoryRawImpl(
    private val mongoTemplate: MongoTemplate,
) : AuthorRepositoryRaw {
    override fun findAuthors(
        names: List<String>,
        minAge: Int?,
        maxAge: Int?,
        nickname: String?,
    ): List<YoungAuthor> {
        val criteriaList = mutableListOf<Criteria>()
        criteriaList.add(Criteria.where("name").`in`(names))
        if (minAge != null && maxAge != null) {
            criteriaList.add(Criteria.where("age").gt(minAge).lt(maxAge))
        } else {
            minAge?.let { criteriaList.add(Criteria.where("age").gt(it)) }
            maxAge?.let { criteriaList.add(Criteria.where("age").lt(it)) }
        }
        nickname?.let { criteriaList.add(Criteria.where("nickname").regex(it, "i")) }

        val query =
            if (criteriaList.isNotEmpty()) {
                val criteria = Criteria().andOperator(criteriaList)
                Query(criteria)
            } else {
                Query()
            }
        return mongoTemplate.find(query, YoungAuthor::class.java)
    }
}