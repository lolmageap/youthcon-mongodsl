package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.YoungAuthor
import com.example.youthconmongodsl.extension.document
import com.example.youthconmongodsl.extension.field
import com.example.youthconmongodsl.extension.findAll
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.MongoRepository

interface AuthorCustomDSLRepository : MongoRepository<YoungAuthor, String>, AuthorRepositoryCustomize

interface AuthorRepositoryCustomize {
    fun findAuthors(
        names: List<String>,
        minAge: Int?,
        maxAge: Int?,
        nickname: String?,
        pageable: Pageable,
    ): Page<YoungAuthor>
}

class AuthorRepositoryCustomizeImpl(
    private val mongoTemplate: MongoTemplate,
) : AuthorRepositoryCustomize {

    override fun findAuthors(
        names: List<String>,
        minAge: Int?,
        maxAge: Int?,
        nickname: String?,
        pageable: Pageable,
    ): Page<YoungAuthor> {
        val document = document {
            and(
                { field(YoungAuthor::name) `in` names },
                { field(YoungAuthor::age) between (minAge to maxAge) },
                { nickname?.let { field(YoungAuthor::nickname) contains it } },
            )
        }
        return mongoTemplate.findAll(document, pageable, YoungAuthor::class)
    }
}