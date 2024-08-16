package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.where
import kotlin.reflect.KClass

@SpringBootTest
class Scenario1(
    private val mongoTemplate: MongoTemplate,
) {
    @Test
    fun `대소문자 구분 없이 별명에 hy가 포함된 저자를 조회합니다 as is`() {
        val criteria = Criteria().andOperator(
            Criteria.where("nickname").regex("hy", "i"),
            Criteria.where("age").gte(30),
        )

        val query = Query(criteria)
        val authors = mongoTemplate.find(query, Author::class.java)
        val nicknameFilteredAuthors = authors.filter { it.nickname?.contains("hy", ignoreCase = true) == true }
        val ageFilteredAuthors = authors.filter { it.age >= 30 }

        Assertions.assertThat(authors.size).isEqualTo(nicknameFilteredAuthors.size)
        Assertions.assertThat(authors.size).isEqualTo(ageFilteredAuthors.size)
    }

    @Test
    fun `대소문자 구분 없이 별명에 hy가 포함된 저자를 조회합니다 to be`() {
        val criteria = andOperator(
            where(Author::nickname).contains("hy", true),
            where(Author::age).gte(30),
        ).toQuery()

        val authors = mongoTemplate.find(criteria, Author::class)
        val nicknameFilteredAuthors = authors.filter { it.nickname?.contains("hy", ignoreCase = true) == true }
        val ageFilteredAuthors = authors.filter { it.age >= 30 }

        Assertions.assertThat(authors.size).isEqualTo(nicknameFilteredAuthors.size)
        Assertions.assertThat(authors.size).isEqualTo(ageFilteredAuthors.size)
    }

    /** 혼잣말 메모장
     *
     * 1. Criteria().andOperator()에서 Criteria를 생성하는 부분은 생략이 가능해보입니다.
     *    andOperator라는 top level 함수로 변경하는 것이 가독성이 더 좋을 것 같습니다.
     *
     * 2. andOperator 안에서 Criteria.where 와 같은 부분이 중복되어 사용되고 있다. 이 부분을 줄일 수 있을 것 같다.
     *
     * 3. where 안에 문자열 리터럴을 넘기고 있습니다.
     *    이런경우 비즈니스로직을 작성할 때 오타가 발견해도 컴파일러가 알려주지 않습니다.
     *    이런 문제를 Kotlin의 Reflection인 KProperty를 사용하여 해결할 수 있습니다.
     *    여기서 KProperty란 코틀린에서 클래스 필드에 접근하여 필드에 대한 정보를 가져올 수 있는 클래스입니다.
     *    이 부분은 직접 구현하는것보다 코틀린에서 지원해주는 where, and 함수를 가져와서 사용하는것이 더 좋을것 같습니다.
     *
     * 4. regex("hy", "i") <- 이게 명시적이지 않습니다..
     *    mongoDB를 사용하지 않는 분들이 보기에는 이게 무슨 의미인지 알기 어려울 수 있습니다.
     *
     * 5. 확장 함수에 대해서 설명하며 Query(criteria) -> criteria.toQuery()로 변경하자.
     *
     * 6. .java도 생략 할 수 있을 것 같다.
     */
}

fun andOperator(
    vararg criteria: Criteria,
) =
    Criteria().andOperator(*criteria)

fun where(field: String) =
    Criteria.where(field)

fun Criteria.contains(
    value: String,
    ignoreCase: Boolean = false,
) =
    regex(value, if (ignoreCase) "i" else null)

fun Criteria.toQuery() =
    Query(this)

fun <T: Any> MongoTemplate.find(
    query: Query,
    clazz: KClass<T>,
):MutableList<T> =
    this.find(query, clazz.java)