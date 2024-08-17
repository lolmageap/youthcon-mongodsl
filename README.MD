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
      uri: mongodb://HOST를_넣어야합니다:27017/youthcon
```

### MongoDB

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

### Kotlin 함수

#### Top Level 함수

Kotlin은 Java와 달리 Top Level 함수를 지원합니다.  
Top Level 함수란 클래스 내부에 정의되지 않고 전역적으로 사용할 수 있는 함수를 의미합니다.

```java
// Java
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

```kotlin
// Kotlin
fun main() {
    println("Hello, World!")
}
```

#### 확장 함수

Kotlin은 확장 함수라는 기능을 제공합니다.  
확장 함수는 클래스 내부가 아닌 외부에서 클래스에 함수를 추가할 수 있습니다.

```kotlin
// 일반 함수
class User(val name: String) {
    fun greet() {
        println("Hello, $name")
    }
}

// 확장 함수
fun User.hi() {
    println("Hi, ${this.name}")
}
```

#### 중위 함수

Kotlin은 인자가 하나뿐인 함수에 infix 키워드를 붙혀 중위 함수란 특별한 종류의 함수를 지원합니다.  
중위 함수를 사용하면 마치 연산자처럼 사용이 가능해지며 코드가 간결해져서 가독성이 좋아집니다.

```kotlin
// 일반 함수
fun Int.sum(b: Int): Int {
    return this + b
}

// 중위 함수
infix fun Int.plus(b: Int): Int {
    return this + b
}

fun main() {
    val three = 1.sum(2)
    val five = 2 plus 3
}
```