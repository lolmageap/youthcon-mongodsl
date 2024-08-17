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
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

/**
 * 1. MongoDB의 쿼리를 작성할 때 앞서 말한 대로 JSON 형태로 작성합니다.
 *    쌍따옴표를 3개 사용하면 문자열을 여러 줄에 걸쳐 작성할 수 있습니다.
 *    trimIndent()는 문자열의 앞, 뒷부분에 있는 공백을 제거합니다.
 *
 * 2. 위와 같은 몽고 쿼리를 Kotlin에서 사용하려면 MongoTemplate과 같은 별도의 Dependency가 필요합니다.
 *    JSON 형태의 문자열 쿼리를 항상 작성하는건 비효율적이기 때문에 이번 실습은 Criteria를 사용해서 쿼리를 작성하려고 합니다.
 *    Criteria는 Spring Data MongoDB에서 제공하는 쿼리 작성을 위한 클래스입니다.
 */

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
        val nativeQuery = BasicQuery(Document.parse(jsonObject))
        mongoTemplate.findOne(nativeQuery, Author::class.java)
    }

    @Test
    fun `MongoTemplate에서 Criteria를 사용한 조회`() {
        val criteria = Criteria().andOperator(
            Criteria.where("name").`is`("youthcon").and("age").`is`(30)
        )
        val query = Query.query(criteria)

        val author = mongoTemplate.findOne(query, Author::class.java)
        Assertions.assertThat(author).isNotNull
    }
}