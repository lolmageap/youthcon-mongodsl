package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.and
import org.springframework.data.mongodb.core.query.where
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * 1. Criteria().andOperator()에서 Criteria를 생성하는 부분은 생략이 가능해보입니다.
 *    andOperator라는 top level 함수로 변경하는 것이 가독성이 더 좋을 것 같습니다.
 *
 * 2. andOperator 안에서 Criteria.where 와 같은 부분이 중복되어 사용되고 있다. 이 부분도 줄일 수 있을 것 같습니다.
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
 * 5. infix 함수는 중위 함수라고도 불리며, 함수를 호출할때 .을 생략해서 간결해지며 가독성이 좋아집니다.
 *    하지만 and, or 과 같은 연산자가 들어가게 된다면 무조건 가독성이 좋아지는것은 아닙니다.
 *    오히려 복잡한 연산을 할 때 infix 함수를 사용한다면 가독성이 떨어질 수 있습니다.
 *    한번 직접 코드로 작성해보고 가독성을 확인해보시죠!
 *
 * 6. 확장 함수에 대해서 설명하며 Query(criteria) -> criteria.toQuery()로 변경하면 가독성이 더 올라갈 것 같습니다.
 *
 * 7. .java도 생략 할 수 있을 것 같습니다.
 *
 * 8. 정렬(sort)을 할때도 with(Sort.by(Sort.Order.asc("age"))) 이 부분도 더 깔끔하게 변경할 수 있을것 같습니다.
 *    우리는 조금 더 명시적으로 orderBy 라는 키워드를 사용해서 함수를 한번 만들어보겠습니다.
 */

@SpringBootTest
class Scenario2(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    @Test
    fun `대소문자 구분 없이 별명에 hy가 포함된 저자를 나이가 많은 순으로 조회한다 as is`() {
        val criteria = Criteria().andOperator(
            Criteria.where("nickname").regex("hy", "i"),
            Criteria.where("age").gte(30),
        )

        val query = Query.query(criteria).with(Sort.by(Sort.Order.desc("age")))
        val authors = mongoTemplate.find(query, Author::class.java)


        // 검증
        val nicknameFilteredAuthors = authors.filter { it.nickname?.contains("hy", ignoreCase = true) == true }
        val ageFilteredAuthors = authors.filter { it.age >= 30 }
        val maxAge = authors.maxOf { it.age }

        Assertions.assertThat(authors.size).isEqualTo(nicknameFilteredAuthors.size)
        Assertions.assertThat(authors.size).isEqualTo(ageFilteredAuthors.size)
        Assertions.assertThat(authors.first().age).isEqualTo(maxAge)
    }

    @Test
    fun `대소문자 구분 없이 별명에 hy가 포함된 저자를 조회합니다 to be`() {
        val criteria = andOperator(
            where(Author::nickname) containsIgnoreCase "hy" and Author::age gte 30,
        ).toQuery()
            .orderBy(Author::age, Sort.Direction.DESC)

        val authors = mongoTemplate.find(criteria, Author::class)


        // 검증
        val nicknameFilteredAuthors = authors.filter { it.nickname?.contains("hy", ignoreCase = true) == true }
        val ageFilteredAuthors = authors.filter { it.age >= 30 }
        val maxAge = authors.maxOf { it.age }

        Assertions.assertThat(authors.size).isEqualTo(nicknameFilteredAuthors.size)
        Assertions.assertThat(authors.size).isEqualTo(ageFilteredAuthors.size)
        Assertions.assertThat(authors.first().age).isEqualTo(maxAge)
    }
}

fun andOperator(
    vararg criteria: Criteria,
) =
    Criteria().andOperator(*criteria)

fun where(field: String) =
    Criteria.where(field)

infix fun Criteria.containsIgnoreCase(
    value: String,
) =
    regex(value, "i")

infix fun Criteria.gte(
    value: Int,
) =
    this.gte(value)

fun Criteria.toQuery() =
    Query(this)

fun <T: Any> MongoTemplate.find(
    query: Query,
    clazz: KClass<T>,
):MutableList<T> =
    this.find(query, clazz.java)

fun Query.orderBy(
    key: KProperty<*>,
    direction: Sort.Direction,
) =
    this.with(
        Sort.by(
            Sort.Order(direction, key.name)
        )
    )