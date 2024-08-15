# 유스콘'24 발표자 오리엔테이션

## 실습 진행 환경

- IntelliJ IDEA
- Java 17, Kotlin 1.9.24
- SpringBoot 3.3.2
- Gradle 8.8
- mongodb 7

## 실습을 하고 얻는 것

1. 자신감.
2. Kotlin에 대한 이해도
3. MongoDB에 대한 이해도

## 발표 대상

1. Kotlin에 관심이 있는 개발자
2. ORM을 사용해본적 있는 개발자
3. 나만의 DSL을 만들어보고 싶은 개발자

## 시나리오

1. 데이터가 들어 있는 MongoDB에 접속합니다.
2. Mongo DSL의 연산에 대해 배웁니다.
3. 통계 쿼리를 생성하는 방법에 대해 배웁니다.
4. 정렬을 하는 방법에 대해 배웁니다.
5. 기존에 Criteria로 작성된 복잡한 코드를 DSL로 리팩토링을 진행할 예정입니다.

### 설정 방법

데이터가 들어 있는 MongoDB URI를 yml 파일에서 설정합니다.

```yaml
spring:
  application:
    name: youthcon-mongodsl
  data:
    mongodb:
      uri: mongodb://HOST를_넣어야합니다:27017/youthcon
```

### 사용법

DSL의 구조는 생각보다 정말 단순합니다.  
`document` 함수를 호출하면 `DocumentScope`가 생성되고 `DocumentScope` 내부에서 `and`, `or`, `not`, `nor` 함수를 호출하여 `Document`를 만들 수 있습니다.  
복잡한 조건 연산을 수행할 수 있게 `and`, `or`, `not`, `nor` 함수 내에서 또 다른 `and`, `or`, `not`, `nor` 함수를 호출할 수도 있습니다.  
chaining 방식으로 복잡한 연산을 수행할 때 발생하는 가독성 문제를 해결할 수 있습니다.  

#### 연산

> #### [연산 Test 바로가기](src%2Ftest%2Fkotlin%2Fcom%2Fexample%2Fyouthconmongodsl%2FFieldTest.kt)

```kotlin
@Test
fun `같은 값을 조회합니다.`() {
    document {
        and(
            { field(Author::name) eq "John" },
            { field(Author::age) eq 18 },
        )
    }
}

@Test
fun `같지 않은 값을 조회합니다.`() {
    document {
        and(
            { field(Author::name) ne "John" },
            { field(Author::age) ne 18 }
        )
    }
}

@Test
fun `큰 값을 조회합니다`() {
    document {
        and { field(Author::age) gt 18 }
    }
}

@Test
fun `작은 값을 조회합니다`() {
    document {
        and { field(Author::age) lt 18 }
    }
}

@Test
fun `사이에 있는 값을 조회합니다`() {
    document {
        and { field(Author::age) between (18 to 30) }
    }
}

@Test
fun `값을 포함하면 조회합니다`() {
    document {
        and { field(Author::age) `in` listOf(18, 19, 20) }
    }
}

@Test
fun `like 연산과 같이 문자열을 포함하면 조회합니다`() {
    document {
        and { field(Author::name) contains "John" }
    }
}

@Test
fun `like 연산과 같지만 대소문자를 구별하지 않고 문자열을 포함하면 조회합니다`() {
    document {
        and { field(Author::name) containsIgnoreCase "John" }
    }
}
```

#### 통계 쿼리 생성

> #### [통계 쿼리 Test 바로가기](src%2Ftest%2Fkotlin%2Fcom%2Fexample%2Fyouthconmongodsl%2FGroupTest.kt)

grouping을 사용하면 sum, avg, min, max, count 등 다양한 통계 쿼리를 생성할 수 있습니다.  
아래 코드는 grouping 대상을 명시하진 않아 조건에 맞는 Author 나이의 전체 합을 구하는 코드입니다.

```kotlin
@Test
fun `전체에 대한 합을 구한다`() {
    val document = document {
        and { field(Author::name) eq "John" }
    }

    val sumOfAge = mongoTemplate.sum(document, Author::age)
    assert(sumOfAge == 100)
}
```

grouping을 명시하면 아래와 같이 group 별로 통계 쿼리를 생성할 수 있습니다.  
그리고 만약 mongodb collection에 field가 string 타입이어도 숫자로 형변환하여 계산하는 것도 가능합니다.  

```kotlin
@Test
fun `grouping 된 필드에 대한 합을 구한다`() {
    val document = document {
        and { field(Author::name) eq "John" }
    }

    val statusGroup = document.groupBy(Author::status)
    val sumOfGroup = mongoTemplate.sum(statusGroup, Author::age)
    assert(sumOfGroup == mapOf(ACTIVE to 90, INACTIVE to 10))
}
```

#### 정렬

> #### [정렬 Test 바로가기](src%2Ftest%2Fkotlin%2Fcom%2Fexample%2Fyouthconmongodsl%2FOrderByTest.kt)

정렬은 orderBy 함수를 사용하고 chaining을 통해 다중 정렬을 할 수 있습니다.

```kotlin
@Test
fun `다중 orderBy 정렬 테스트`() {
    val document = document {
        and(
            { field(Author::name) eq "John" },
            { field(Author::age) eq 18 },
        )
    }.orderBy(Author::name).desc()
        .orderBy(Author::age).asc()

    assert(document.sortObject == Document("name", -1).append("age", 1))
}
```

## Hands-On 리팩토링 실습

Criteria로 작성된 쿼리를 DSL로 리팩토링하는 실습을 진행합니다.

1. 간단한 쿼리
    - 이름이 cherhy이고 나이가 18살인 저자를 조회합니다.
    - 대소문자 구분 없이 별명에 hy가 포함된 저자를 조회합니다.

2. and, or, not, nor 연산이 포함된 복잡한 쿼리
    - 별명이 존재하지 않는 작가 또는 나이가 50 이상이지만 키가 160cm 이하인 작가를 조회한다.
    - 나이가 20대 이고 상태가 ACTIVE 여야하고 또는 나이가 61살 이상 90살 미만이고 상태가 REST, RETIREMENT 여야합니다.

3. embedding된 document를 기반으로 조회하는 쿼리
    - 책의 제목이 Kotlin 이고 저자의 이름이 cherhy 인 책을 조회합니다.

4. 통계 쿼리
    - 책을 3권 이상 낸 30대 이상의 작가의 돈의 총합을 구합니다.
    - 책을 3권 이상 낸 30대 이상의 작가를 상태 별로 그루핑하여 돈의 합을 구합니다.

5. 동적 조건을 포함한 쿼리
   - nullable한 필드를 포함한 검색용 DTO로 쿼리를 작성합니다.