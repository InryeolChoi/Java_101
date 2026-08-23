# 순수 JDBC의 불편함과 추상화

> 전체 JDBC 실습을 마친 뒤 작성할 회고 문서다.

## 교육과정에 JDBC가 아직 필요한 이유

JDBC 코드를 계속 손으로 쓰는 것이 목표는 아니다. Spring JDBC와 JPA를 쓰더라도 SQL 실행, DB 세션, Connection, 트랜잭션, 제약조건은 사라지지 않는다. 다만 프레임워크가 그 일을 대신 관리해준다.

그래서 JDBC를 한 번 직접 해보면 다음 상황에서 판단할 근거가 생긴다.

- `@Transactional`이 붙었는데 왜 두 DB 작업이 같이 rollback되지 않는지
- JPA가 실행한 SQL이 왜 느리거나 예상보다 많이 나가는지
- UNIQUE, FK 같은 DB 제약조건 예외가 어디에서 발생한 것인지
- Connection pool이 왜 필요하고 Connection을 아무 곳에서나 오래 잡으면 왜 문제가 되는지
- 단순 조회나 대량 처리에서 JPA 대신 JDBC 또는 Spring JDBC를 선택할 이유가 있는지

AI가 CRUD 코드를 빠르게 만들 수 있어도, 어떤 작업을 하나의 트랜잭션으로 묶을지, DB 제약조건을 어디에 둘지, 생성된 SQL이 실제로 맞는지는 개발자가 판단해야 한다. 이 실습은 그 판단을 위한 바닥을 만드는 과정이다.

## JDBC를 직접 또는 가깝게 쓰는 경우

대부분의 일반적인 Spring 프로젝트는 JPA나 Spring Data JPA로 시작해도 된다. 그렇다고 JDBC 계열이 사라지는 것은 아니다.

- 대량 INSERT/UPDATE처럼 batch 처리가 중요한 경우
- DB 전용 함수, 복잡한 통계·리포트 SQL, stored procedure를 써야 하는 경우
- JPA가 만든 SQL을 더 세밀하게 제어해야 하는 경우
- 이미 JDBC 또는 SQL 중심으로 만들어진 레거시 시스템을 다루는 경우

이때도 보통은 순수 JDBC보다 Spring JDBC의 `JdbcTemplate`을 먼저 고려한다. 순수 JDBC 실습은 그 아래에서 `JdbcTemplate`이 줄여주는 반복을 보기 위한 것이다.

## Spring 책을 읽을 때 연결할 지점

지금 실습은 이후 Spring 책의 다음 내용을 읽을 때 기준점이 된다.

| Spring에서 보는 것 | 지금 직접 확인하는 것 |
|---|---|
| DataSource / Connection Pool | `DriverManager.getConnection()`으로 얻은 Connection과 DB 세션 |
| Service와 `@Transactional` | `setAutoCommit(false)`, `commit()`, `rollback()`으로 정하는 트랜잭션 경계 |
| Repository / DAO | `MemberDao` 안의 SQL, PreparedStatement, ResultSet 매핑 |
| JPA Entity | 지금 `new Member(...)`로 하는 행 → 객체 수동 매핑 |
| Controller와 DTO | HTTP 요청·응답 경계의 데이터. 지금 콘솔 JDBC 단계에서는 아직 불필요 |

Controller나 `@RestController`는 외부 요청을 받는 입구다. DB 변경을 어디까지 하나의 작업으로 볼지와 그 작업을 안전하게 commit/rollback할지는 서비스와 트랜잭션의 문제다. 따라서 Spring의 핵심을 단순 어노테이션 문법보다 DB와 트랜잭션의 연결까지 포함해 이해하는 것이 맞다.

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
