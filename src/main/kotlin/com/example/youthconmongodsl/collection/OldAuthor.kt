//package com.example.youthconmongodsl.collection
//
//import com.querydsl.core.annotations.QueryEntity
//import jakarta.persistence.Entity
//import jakarta.persistence.EnumType
//import jakarta.persistence.Enumerated
//import jakarta.persistence.Id
//import org.bson.types.ObjectId
//import org.springframework.data.mongodb.core.mapping.Document
//import org.springframework.data.mongodb.core.mapping.Field
//
//@Entity
//@QueryEntity
//@Document(collection = "author")
//data class OldAuthor(
//    @Id @Field("_id")
//    val id: String = ObjectId.get().toHexString(),
//    val name: String,
//    val nickname: String,
//    val age: Int,
//    val weight: Double,
//    val height: Float,
//    @Enumerated(EnumType.STRING)
//    val status: Status,
//    val books: MutableList<String> = mutableListOf(),
//)