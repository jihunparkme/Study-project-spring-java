# Lambda

## 람다 실습 1 - 익명 클래스를 람다로 전환
- [x] MoveStrategy에 대한 익명 클래스 -> 람다 적용
  - nextstep.fp.CarTest의 이동, 정지 method

## 람다 실습 2 - 람다를 활용해 중복 제거

- nextstep.fp.Lambda 중복 제거
  - [x] sumAll
  - [x] sumAllEven
  - [x] sumAllOverThree

**Hint**

- [x] 변경되는 부분과 변경되지 않는 부분의 코드를 분리
- [x] 변경되는 부분을 인터페이스로 추출
- [x] 인터페이스에 대한 구현체를 익명 클래스(anonymous class)로 구현해 메소드의 인자로 전달

- 인터페이스로 추출

```text
public interface Conditional { 
    boolean test(Integer number);
}
```

- Conditional을 활용한 공통 메소드의 구조

```text
public int sumAll(List<Integer> numbers, Conditional c) {
    // c.test(number)를 활용해 구현할 수 있다.
}
```

# Stream

## stream method 실습

map, reduce, filter 실습 1

- [x] List에 담긴 모든 숫자 중 3보다 큰 숫자를 2배 한 후 모든 값의 합을 구하기
  - 지금까지 학습한 map, reduce, filter를 활용해 구현해야 한다. 
  - nextstep.fp.StreamStudyTest 클래스의 sumOverThreeAndDouble() 테스트를 pass해야 한다.

## map, reduce, filter 실습 2

src/main/resources/fp 디렉토리 아래에 있는 war-and-peace.txt 파일을 읽어 다음 요구사항을 만족

nextstep.fp.StreamStudy 클래스의 printLongestWordTop100() 메서드를 구현한다.

- [x] 단어의 길이가 12자를 초과하는 단어를 추출한다.
- [x] 12자가 넘는 단어 중 길이가 긴 순서로 100개의 단어를 추출한다.
- [x] 단어 중복을 허용하지 않는다. 즉, 서로 다른 단어 100개를 추출해야 한다.
- [x] 추출한 100개의 단어를 출력한다. 모든 단어는 소문자로 출력해야 한다.

# Optional

## 요구사항 1 - Optional을 활용해 조건에 따른 반환

```text
nextstep.optional.User의 ageIsInRange1() 메소드는 30살 이상, 45살 이하에 해당하는 User가 존재하는 경우 true를 반환하는 메소드이다.

같은 기능을 Optional을 활용해 ageIsInRange2() 메소드에 구현한다. 메소드 인자로 받은 User를 Optional로 생성하면 stream의 map, filter와 같은 메소드를 사용하는 것이 가능하다.

nextstep.optional.UserTest의 테스트가 모두 pass해야 한다.
```

Hint 

- [x] Guide To Java 8 Optional 문서를 참고해 Optional 사용 방법을 익힌다.
- [x] Optional.ofNullable(user)을 활용해 User을 Optional로 생성하는 것이 가능하다.
- [x] Optional의 map(), filter() 메소드등을 활용해 필요한 데이터를 추출
- [x] Optional의 isPresent() 메소드 활용

## 요구사항 2 - Optional에서 값을 반환

```text
nextstep.optional.Users의 getUser() 메소드를 자바 8의 stream과 Optional을 활용해 구현한다.

자바 8의 stream과 Optional을 사용하도록 리팩토링한 후 UsersTest의 단위 테스트가 통과해야 한다.
```

Hint

- [x] Guide To Java 8 Optional 문서를 참고해 Optional 사용 방법을 익힌다.
- [x] Optional의 orElse() 메소드 활용해 구현한다.

## 요구사항 3 - Optional에서 exception 처리

```text
nextstep.optional.ExpressionTest의 테스트가 통과하도록 Expression의 of 메소드를 구현한다.

단, of 메소드를 구현할 때 자바 8의 stream을 기반으로 구현한다.
```
Hint

- [x] Guide To Java 8 Optional 문서를 참고해 Optional 사용 방법을 익힌다.
- [x] 자바의 enum 전체 값은 values() 메소드를 통해 배열로 접근 가능하다.
- [x] Arrays.stream()을 이용해 배열을 stream으로 생성할 수 있다.
- [x] 일치하는 값 하나를 추출할 때 findFirst() 메소드를 활용 가능하다.
- [x] Optional의 orElseThrow() 메소드 활용해 구현한다.
