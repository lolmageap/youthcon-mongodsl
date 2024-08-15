package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

@SpringBootTest
class Scenario1(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    /** 혼잣말 메모장
     * 1. as is 같은 경우 regex("hy", "i") <- 이게 명시적이지 않다.
     *    mongoDB를 사용하지 않는 사람들이 보기에는 뭔뜻인지 모른다. TO BE 에서 수정하자.
     *
     * 2. 확장 함수에 대해서 설명하며 Criteria().andOperator() 이렇게 사용하는 것 보다 infix 함수를 사용하여 Criteria().and {} 이렇게 함수식을 넘기면 더 가독성이 좋을 것 같다.
     * 3. andOperator 안에서 Criteria.where 와 같은 부분이 중복되어 사용되고 있다. 이 부분을 줄일 수 있을 것 같다.
     * 4. 확장 함수에 대해서 설명하며 Query(criteria) -> criteria.toQuery()로 변경하자.
     * 5. .java도 생략 할 수 있을 것 같다.
     */
    @Test
    fun `대소문자 구분 없이 별명에 hy가 포함된 저자를 조회합니다`() {
        // as is
        val criteriaV1 = Criteria().andOperator(
            Criteria.where("nickname").regex("hy", "i"),
        )

        // to be
        val criteriaV2 = Criteria() and {
            where("nickname").contains("hy", true)
        }

        val authors = mongoTemplate.find(criteriaV2.toQuery(), Author::class.java)

        val filteredAuthor = authors.filter { it.nickname?.contains("hy", ignoreCase = true) == true }
        assert(authors.size == filteredAuthor.size)
        assert(authors.isNotEmpty())
    }
}

infix fun Criteria.and (
    block: Criteria.() -> Criteria
) =
    this.andOperator(block())

fun where(field: String) =
    Criteria.where(field)

fun Criteria.contains(value: String, ignoreCase: Boolean = true) =
    regex(value, if (ignoreCase) "i" else "")

fun Criteria.toQuery() =
    Query(this)