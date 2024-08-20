package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import org.assertj.core.api.Assertions
import org.bson.Document
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.BasicQuery

@SpringBootTest
class Scenario1(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    @Test
    fun `MongoTemplate에서 Native Query를 사용한 조회`() {
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
        Assertions.assertThat(author).isNotNull
    }
}