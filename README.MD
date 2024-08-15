# 유스콘'24 발표자 오리엔테이션

## 실습 진행 환경

- IntelliJ IDEA
- Java 17, Kotlin 1.9.24
- SpringBoot 3.3.2
- Gradle 8.8
- mongodb 7

## 실습을 하고 얻는 것

1. Kotlin에 대한 이해도
2. 함수형 프로그래밍에 대한 이해도
3. MongoDB에 대한 이해도

## 발표 대상

1. Kotlin에 관심이 있는 개발자
2. 함수형 프로그래밍에 관심이 있는 개발자
3. 나만의 DSL을 만들어보고 싶은 개발자

## 메뉴얼

1. 데이터가 들어 있는 MongoDB에 연결합니다.
2. 코틀린에서 제공하는 탑레벨 함수, 확장 함수, 중위 함수, 고차 함수에 대해서 학습합니다.
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

### 탑레벨 함수
```kotlin

```

### 확장 함수
```kotlin

```

### 중위 함수
```kotlin

```

### 고차 함수
```kotlin

```

### 시나리오
Criteria로 작성된 코드를 DSL로 리팩토링합니다.

- 이름이 "cherhy" 이고 나이가 18살인 저자를 조회합니다.
- 책을 3권 이상 낸 30대 이상의 작가를 상태 별로 그루핑하여 돈의 합을 구합니다.
- 나이가 20대 이고 상태가 ACTIVE 여야하고 또는 나이가 61살 이상 90살 미만이고 상태가 REST, RETIREMENT인 작가를 조회합니다.