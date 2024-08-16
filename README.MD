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
2. 리팩토링을 좋아하는 개발자
3. 나만의 DSL을 만들어보고 싶은 개발자

## 메뉴얼

1. 데이터가 들어 있는 MongoDB에 연결합니다.
2. 코틀린에서 제공하는 탑레벨 함수, 확장 함수, 중위 함수에 대해서 학습합니다.
3. 기존에 Criteria로 작성된 복잡한 코드를 DSL로 리팩토링합니다.

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

### Kotlin 함수

#### 탑레벨 함수

Kotlin은 Java와 달리 탑레벨 함수를 지원합니다.  
탑레벨 함수는 클래스 내부에 정의되지 않고 전역적으로 사용할 수 있는 함수를 의미합니다.

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

인자가 하나뿐인 함수에는 중위 함수란 특별한 종류의 함수를 정의할 수 있습니다.  
중위 함수 가독성이 좋아지고 코드가 간결해지는 장점이 있고 infix 라는 키워드를 사용하여 정의합니다.  

```kotlin
// 일반 함수
fun sum(a: Int, b: Int): Int {
    return a + b
}

// 중위 함수
infix fun Int.plus(b: Int): Int {
    return this + b
}

fun main() {
    sum(1, 2)
    1 plus 2
}
```

### 시나리오

- 좀전에 배운 Kotlin 함수를 활용하여 Criteria로 작성된 코드를 DSL로 리팩토링합니다.
- 간단한 Criteria 코드를 Kotlin DSL로 변경합니다.
- 조금 복잡한 쿼리를 Kotlin DSL로 변경합니다.