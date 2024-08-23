# 유스콘'24 발표자 오리엔테이션

## 실습 진행 환경

- IntelliJ IDEA
- Java 17, Kotlin 1.9.24
- SpringBoot 3.3.2
- Gradle 8.8
- mongodb 7

## 실습을 하고 얻는 것

1. Kotlin에 대한 이해도
2. MongoDB에 대한 이해도
3. 코드를 간결하게 작성하는 방법

## 발표 대상

1. Kotlin에 관심이 있는 개발자
2. MongoDB에 관심이 있는 개발자
3. 개선하는 것을 좋아하는 개발자
4. 나만의 DSL을 만들어보고 싶은 개발자

## 시나리오

1. 데이터가 들어 있는 MongoDB에 연결합니다.
2. Kotlin에서 제공하는 탑레벨 함수, 확장 함수, 중위 함수에 대해서 학습합니다.
3. Spring, Kotlin으로 MongoDB 데이터를 조회하는 방법을 학습합니다.
4. 기존에 Criteria로 작성된 복잡한 코드를 DSL로 리팩토링합니다.

### 설정 방법

데이터가 들어 있는 MongoDB URI를 yml 파일에서 설정합니다.

```yaml
spring:
  application:
    name: youthcon-mongodsl
  data:
    mongodb:
      uri: mongodb://MONGO_ATLAS_HOST:27017/youthcon
```

### ⭐️ MongoDB

RDBMS는 Table 이라는 구조로 데이터를 저장하지만 MongoDB는 Collection 이라는 구조로 데이터를 저장합니다.  
Collection은 아래처럼 Binary JSON 형태로 데이터를 저장합니다.  
RDBMS와 다르게 복잡한 관계가 없는 데이터를 저장할 때 사용하면 좋습니다.  
실습에서 진행할 MongoDB Collection 예시입니다.

```json
{
  "_id": "66ba029d667d035a53c2eb57",
  "name": "정철희",
  "age": 25,
  "status": "ACTIVE",
  "nickname": "유스콘",
  "weight": 65.0,
  "height": 170.0,
  "money": 1000,
  "books": [
    {
      "title": "Kotlin DSL",
      "price": 10000,
      "isbn": "1234567890",
      "description": "Kotlin DSL에 대한 설명"
    },
    {
      "title": "SpringBoot",
      "price": 20000,
      "isbn": "0987654321"
    }
  ]
}
```

### 🔥 실습

#### 1번 시나리오 - MongoDB 이해

MongoDB에 구조를 파악하고 데이터를 조회하는 방법을 학습합니다.  
이후 Criteria로 코드를 리팩토링합니다.  

```kotlin
    val json = """
        {
            "name": "youthcon",
            "age": 30
        }
    """.trimIndent()

    val jsonObject = JSONObject(json).toString()
    val query = BasicQuery(Document.parse(jsonObject))
```

#### 2번 시나리오 - Criteria 리팩토링

Criteria로 작성된 코드를 DSL로 리팩토링합니다.  
이때 Kotlin이 지원하는 탑레벨 함수, 확장 함수, 중위 함수에 대해 학습합니다.

```kotlin
    // 닉네임에 hy가 들어가고 나이가 30세 이상인 작가를 나이를 기준으로 내림차순으로 정렬한다.
    val criteria = Criteria().andOperator(
        Criteria.where("nickname").regex("hy", "i"),
        Criteria.where("age").gte(30),
    )

    val query = Query(criteria).with(Sort.by(Sort.Order(DESC, "age")))
    val authors = mongoTemplate.find(query, Author::class.java)
```

#### 3번 시나리오 - Criteria 집계 리팩토링

Criteria로 작성된 집계 코드를 DSL로 리팩토링합니다.  
MongoDB의 집계를 모르는 사람도 이해할 수 있도록 코드를 작성합니다.  

```kotlin
    // 나이가 20대이고 책을 3권 낸 작가를 상태별로 그루핑한 뒤 가진 돈의 합을 구한다.
    val criteria = Criteria().andOperator(
        Criteria.where("age").gte(20).lte(29),
        Criteria.where("books").size(3),
    )

    val matchOperation = MatchOperation(criteria)
    val groupOperation = Aggregation.group("status").sum("money").`as`("totalMoney")
    val aggregation = Aggregation.newAggregation(matchOperation, groupOperation)

    val aggregationResults = mongoTemplate.aggregate(aggregation, Author::class.java, Map::class.java)
```