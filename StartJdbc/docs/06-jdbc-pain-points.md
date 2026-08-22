# 순수 JDBC의 불편함과 추상화

> 전체 JDBC 실습을 마친 뒤 작성할 회고 문서다.

## 순수 JDBC에서 직접 해야 한 일

실습하면서 실제로 반복한 내용을 추가한다.

- Connection 획득
- SQL 문자열 작성
- Statement 또는 PreparedStatement 생성
- 파라미터 바인딩
- ResultSet 순회
- SQL 결과를 Java 객체로 매핑
- 리소스 역순 close
- SQLException 처리
- commit과 rollback 처리

## 비교할 질문

### Spring JDBC

- 어떤 리소스 정리 코드가 사라지는가?
- SQLException은 어떤 예외로 변환되는가?
- SQL과 객체 매핑은 얼마나 남는가?

### JPA/Hibernate

- INSERT와 SELECT SQL을 누가 만드는가?
- ResultSet 매핑은 어디로 사라지는가?
- 영속성 컨텍스트와 변경 감지는 무엇을 추가로 제공하는가?
- JPA를 사용해도 JDBC와 Connection이 사라지는 것은 아닌 이유는 무엇인가?

### Spring Data JPA

- Repository 구현 코드 중 무엇이 사라지는가?
- 메서드 이름 기반 쿼리는 어디까지 편리한가?
- 추상화가 높아져도 SQL과 DB 이해가 필요한 상황은 언제인가?

## 최종 비교표

실습 완료 후 실제 경험을 바탕으로 채운다.

| 관심사 | 순수 JDBC | Spring JDBC | JPA/Hibernate | Spring Data JPA |
|---|---|---|---|---|
| Connection 관리 | 직접 |  |  |  |
| SQL 작성 | 직접 |  |  |  |
| 파라미터 바인딩 | 직접 |  |  |  |
| 결과 매핑 | 직접 |  |  |  |
| 리소스 close | 직접 |  |  |  |
| 트랜잭션 | 직접 |  |  |  |

## 블로그로 옮길 때 강조할 내용

- 처음 예상했던 동작과 실제 결과의 차이
- 직접 작성하며 반복된 코드
- 실수했던 resource close와 null 처리
- Spring/JPA를 배운 뒤 다시 봤을 때 사라진 코드
- 편리한 추상화 뒤에 여전히 JDBC와 DB가 존재한다는 점
