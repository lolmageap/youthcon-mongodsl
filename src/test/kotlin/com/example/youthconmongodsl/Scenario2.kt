package com.example.youthconmongodsl

import com.example.youthconmongodsl.collection.Author
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.mapping.toDotPath
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import kotlin.reflect.KProperty

@SpringBootTest
class Scenario2(
    @Autowired private val mongoTemplate: MongoTemplate,
) {
    @Test
    @DisplayName("대소문자 구분 없이 별명에 hy가 포함되고 나이가 20대인 작가를 나이가 많은 순으로 조회한다")
    fun asIs() {
        val query = where {
            Author::nickname containsIgnoreCase "hy"
            Author::age between 20..29
        } order {
            Author::age by DESC
        }

        // 조회
        val authors = mongoTemplate.find(query, Author::class.java)

        // 검증
        val nicknameFilteredAuthors = authors.filter { it.nickname?.contains("hy", ignoreCase = true) == true }
        val ageFilteredAuthors = authors.filter { it.age in 20..29 }

        assertThat(authors.size).isEqualTo(nicknameFilteredAuthors.size)
        assertThat(authors.size).isEqualTo(ageFilteredAuthors.size)
        assertThat(authors.first().age).isEqualTo(29)
    }
}

class CriteriaBuilder {
    val criteriaList = mutableListOf<Criteria>()

    /**
     * containsIgnoreCase 함수를 사용 하는 곳을 보면
     * KProperty 는 Author::nickname, value 는 "hy" 이 됩니다.
     */
    infix fun KProperty<*>.containsIgnoreCase(
        value: String,
    ): Criteria {
        val criteria = Criteria(this.toDotPath()).regex(value, "i")
        criteriaList.add(criteria)
        return criteria
    }

    /**
     * between 함수를 사용 하는 곳을 보면
     * KProperty 는 Author::age, value.first 는 20, value.last 는 29가 됩니다.
     */
    infix fun KProperty<*>.between(
        value: IntRange,
    ): Criteria {
        val criteria = Criteria(this.toDotPath()).gte(value.first).lte(value.last)
        criteriaList.add(criteria)
        return criteria
    }
}

fun where(
    block: CriteriaBuilder.() -> Unit,
): Criteria {
    val criteriaBuilder = CriteriaBuilder()
    criteriaBuilder.block()
    return Criteria().andOperator(criteriaBuilder.criteriaList)
}

class Order(
    private val criteria: Criteria,
) {
    /**
     * by 함수를 사용 하는 곳을 보면
     * KProperty 는 Author::age, direction 은 DESC(내림차순)이 됩니다.
     */
    infix fun KProperty<*>.by(
        direction: Direction,
    ): Query {
        return Query(criteria).with(Sort.by(Sort.Order(direction, this.toDotPath())))
    }
}

/**
 * order { } scope 는 Order class 의 확장 함수를 매개 변수로 넘기기 때문에
 * Order class 내부에서 함수를 사용한 것 처럼 코드를 작성할 수 있습니다.
 * 그리고 order scope 마지막 줄은 Query Type 을 위치 시켜야 합니다.
 * by 함수는 Query Type 을 반환 하기 때문에 문제 없이 실행 됩니다.
 */
infix fun Criteria.order(
    block: Order.() -> Query,
): Query {
    val order = Order(this)
    return order.block()
}