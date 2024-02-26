## POJO 상품 등록 기능 구현하기

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

.

**Move Inner class for test to Upper level**

테스트 코드에 작성된 inner class 들을 main class 로 이동

## 스프링부트 테스트로 전환하기

순수 자바로 구현된 서비스를 스프링 빈으로 등록하고 스프링 부트 테스트로 동작하도록 전환
- service, adapter, repository 클래스에 @Component 선언
- 기존 테스트에 @SpringBootTest 선언 및 서비스는 @Autowired 로 주입 

---

Point.
- final keyword
- record class
- Assert in constructor