//package com.example.youthconmongodsl
//
//import com.example.youthconmongodsl.collection.OldAuthor
//import com.example.youthconmongodsl.collection.QOldAuthor
//import com.querydsl.core.types.dsl.BooleanExpression
//import org.springframework.data.domain.Page
//import org.springframework.data.domain.Pageable
//import org.springframework.data.mongodb.repository.MongoRepository
//import org.springframework.data.querydsl.QuerydslPredicateExecutor
//
//interface AuthorPredicateBuilder {
//    fun findAuthors(
//        names: List<String>,
//        minAge: Int?,
//        maxAge: Int?,
//        nickname: String?,
//    ): BooleanExpression
//}
//
//class AuthorPredicateBuilderImpl : AuthorPredicateBuilder {
//    override fun findAuthors(
//        names: List<String>,
//        minAge: Int?,
//        maxAge: Int?,
//        nickname: String?,
//    ): BooleanExpression {
//        val author = QOldAuthor.oldAuthor
//        var predicate = author.name.`in`(names)
//        if (minAge != null && maxAge != null) {
//            predicate = predicate.and(
//                author.age.gt(minAge)
//                    .and(author.age.lt(maxAge))
//            )
//        } else {
//            minAge?.let { predicate = predicate.and(author.age.gt(it)) }
//            maxAge?.let { predicate = predicate.and(author.age.lt(it)) }
//        }
//        nickname?.let { predicate = predicate.and(author.nickname.contains(it)) }
//        return predicate
//    }
//}
//
//interface AuthorRepositoryQueryDSL : MongoRepository<OldAuthor, String>, QuerydslPredicateExecutor<OldAuthor>
//
//class AuthorService(
//    private val authorRepository: AuthorRepositoryQueryDSL,
//    private val authorPredicateBuilder: AuthorPredicateBuilder,
//) {
//    fun findAuthors(
//        names: List<String>,
//        minAge: Int?,
//        maxAge: Int?,
//        nickname: String?,
//        pageable: Pageable,
//    ): Page<OldAuthor> {
//        val predicate = authorPredicateBuilder.findAuthors(names, minAge, maxAge, nickname)
//        return authorRepository.findAll(predicate, pageable)
//    }
//}