package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.extension.document
import com.example.youthconmongodsl.extension.field
import com.example.youthconmongodsl.extension.findAll
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.MongoRepository

interface AuthorCustomDSLRepository : MongoRepository<Author, String>, AuthorRepositoryCustomize

interface AuthorRepositoryCustomize {
    fun findAuthors(
        names: List<String>,
        minAge: Int?,
        maxAge: Int?,
        nickname: String?,
        pageable: Pageable,
    ): Page<Author>
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
    ): Page<Author> {
        val document = document {
            and(
                { field(Author::name) `in` names },
                { field(Author::age) between (minAge to maxAge) },
                { nickname?.let { field(Author::nickname) contains it } },
            )
        }
        return mongoTemplate.findAll(document, pageable, Author::class)
    }
}