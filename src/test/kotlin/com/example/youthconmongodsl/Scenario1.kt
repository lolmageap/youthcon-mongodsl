package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import org.assertj.core.api.Assertions.assertThat
import org.bson.Document
import org.json.JSONObject
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.BasicQuery
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

@SpringBootTest
class Scenario1(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    @Test
    @DisplayName("MongoTemplate에서 Native Query를 사용한 조회")
    fun nativeQuery() {
        val json = """
            {
                "name": "youthcon",
                "age": 30
            }
        """.trimIndent()

        val jsonObject = JSONObject(json).toString()
        val query = BasicQuery(Document.parse(jsonObject))


        // 검증
        val author = mongoTemplate.findOne(query, Author::class.java)
        assertThat(author).isNotNull
    }

    @Test
    @DisplayName("MongoTemplate에서 Criteria API를 사용한 조회")
    fun criteriaApi() {
        val criteria = Criteria().andOperator(
            Criteria.where("name").`is`("youthcon").and("age").`is`(30)
        )
        val query = Query.query(criteria)

        // 검증
        val author = mongoTemplate.findOne(query, Author::class.java)
        assertThat(author).isNotNull
    }
}