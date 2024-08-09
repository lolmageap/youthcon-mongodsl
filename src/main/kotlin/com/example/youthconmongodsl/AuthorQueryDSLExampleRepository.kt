package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import com.example.youthconmongodsl.collection.QAuthor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface AuthorRepositoryQueryDSL : MongoRepository<Author, String>, QuerydslPredicateExecutor<Author>

interface AuthorRepositoryPredicate {
    fun findAuthors(
        names: List<String>,
        minAge: Int?,
        maxAge: Int?,
        nickname: String?,
        pageable: Pageable,
    ): Page<Author>
}

class AuthorRepositoryQueryDSLImpl(
    private val authorRepository: AuthorRepositoryQueryDSL,
) : AuthorRepositoryPredicate {

    override fun findAuthors(
        names: List<String>,
        minAge: Int?,
        maxAge: Int?,
        nickname: String?,
        pageable: Pageable,
    ): Page<Author> {
        val author = QAuthor.author
        var predicate = author.name.`in`(names)
        if (minAge != null && maxAge != null) {
            predicate = predicate.and(
                author.age.gt(minAge)
                    .and(author.age.lt(maxAge))
            )
        } else {
            minAge?.let { predicate = predicate.and(author.age.gt(it)) }
            maxAge?.let { predicate = predicate.and(author.age.lt(it)) }
        }
        nickname?.let { predicate = predicate.and(author.nickname.contains(it)) }
        return authorRepository.findAll(predicate, pageable)
    }
}