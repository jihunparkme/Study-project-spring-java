# TDD

[실전! 스프링부트 상품-주문 API 개발로 알아보는 TDD](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-%EC%8B%A4%EC%A0%84-%EC%83%81%ED%92%88%EC%A3%BC%EB%AC%B8-tdd/dashboard) 강의 정리 

## 상품 등록 기능 구현하기

### POJO

**Java TDD**

(1) 필요한 서비스 호출
```java
productService.addProduct(request);
```
- 특정 동작에 필요한 기능들만 구성 가능

(2) 서비스 호출에 필요한 Request 클래스 생성
```java
final AddProductRequest request = new AddProductRequest(name, price, discountPolicy);
```
- Request 클래스는 record 클래스로 사용 가능
- Assert 를 활용해서 생성자 파라미터 검증

(3) 서비스 클래스 생성
- 엔티티 생성

(4) 어댑터 생성

(5) 레파지포리 생성
- 테스트에서는 데이터를 인메모리(Map)로 관리

> Then -> Given -> When 순서로 적성해 보기
> 
> [ProductService addProduct test]()

.

**Move Inner class for test to Upper level**

테스트 코드에 작성된 inner class 들을 main class 로 이동

> [Move Inner class for test to Upper leve]()

### 스프링부트 테스트로 전환하기

순수 자바로 구현된 서비스를 스프링 빈으로 등록하고 스프링 부트 테스트로 동작하도록 전환
- service, adapter, repository 클래스에 @Component 선언
- 기존 테스트에 @SpringBootTest 선언 및 서비스는 @Autowired 로 주입 

> [스프링부트 테스트로 전환하기]()

### API 테스트로 전환하기

API 테스트를 위해 io.rest-assured:rest-assured 사용하기
- rest-assured 는 격리가 잘 안 되는 문제가 존재
  - 데이터가 캐싱이 되어 table sequence 가 꼬여 다른 테스트가 깨질 수 있음(ex. table sequence)
  - 격리 방법은 [3월 우아한테크세미나 / 우아한ATDD](https://www.youtube.com/watch?v=ITVpmjM4mUE) 참고

> [API 테스트로 전환하기]()
>
> [in-memory to jpa]()
>
> [rest-assured 테스트 격리 코드 추가]()

### JPA 적용하기

기존 in-memory 형태의 데이터 저장소를 jpa 로 적용

> [JPA 적용하기]()

## 상품 조회 기능 구현하기

상품 등록 구현으로 이미 기반이 잡혀 있으므로 POJO 부터 테스트를 시작하지 않고 @SpringBootTest 로 시작

> [상품 조회 기능 구현하기]()
>
> [상품 조회 기능 API 테스트로 전환]()

## 상품 수정 기능 구현하기

### POJO

상품 등록과 동일한 방식으로 진행.

(1) 필요한 서비스 호출
```java
productService.updateProduct(productId, request);
```

(2) 서비스 호출에 필요한 Request record 클래스 생성
- Assert 를 활용해서 생성자 파라미터 검증
```java
final UpdateProductRequest request = new UpdateProductRequest(name, price, discountPolicy);
```

(3) 서비스에 상품 수정 메서드 구현

(4) 상품 수정을 위한 도메인 메서드는 테스트 필수
- Product.update

(4) StubClass 또는 Mockito 를 활용하여 테스트

> [상품 수정 기능 구현 및 Stub 클래스를 활용한 테스트]()
> 
> [Use Mockito instead of StubClass]()

### 스프링부트 테스트로 전환하기

> [POJO Test to SpringBoot Test]()

### API 테스트로 전환하기

> [API 테스트로 전환하기]()

## 상품 주문 기능 구현하기

**POJO**

**스프링부트 테스트로 전환하기**

**API 테스트로 전환하기**

**JPA 적용**

## 주문 결제 기능 구현하기

**POJO**

**스프링부트 테스트로 전환하기**


**API 테스트로 전환하기**

**JPA 적용**

---

Point.
- final keyword
- record class
- Assert in constructor
- rest-assured api test
- var type
- HttpStatus
  - 200 OK
  - 201 CREATE
  
---

Reference.

**intellij introduce variable final**
- Preference -> Editor -> Code Style -> Java -> Code Generation -> Final Modifier
  - Make generated local variables final
  - Make generated parameters final

**Live Templates**
- Preferences -> Editor -> Live Templates

**Inspections**
- Preferences -> Editor -> Inspections
  - Local variable or parameter can be final

**extract variable final**
- option + command + v
- option + shift + O
  - Declare final
  - Declare var

**extract method final**
- option + command + m
- option + shift + O
  - make static

**code vision usages**
- Preferences -> Editor -> Inlay Hints -> Java -> Code vision -> Show hints for
  - Usages
  - inheritors


---

TODO.

> commit 이력 링크로 남기기