# 왜 JDBC와 DB를 끝까지 알아야 하나

JPA나 Spring Data JPA를 쓴다고 JDBC가 사라지는 것은 아니다. 애플리케이션은 결국 JDBC 드라이버를 통해 DB에 SQL을 보내고, Connection 안에서 트랜잭션을 시작하고 끝낸다. JPA는 그 과정을 편하게 만들어주지만 DB의 동작을 대신 결정해주지는 않는다.

그래서 이 실습의 목표는 JDBC API를 외우는 것이 아니라, 프레임워크가 감춘 아래 질문을 읽을 수 있게 되는 것이다.

```text
이 SQL은 어느 Connection에서 실행됐나?
언제 commit되나?
실패하면 어떤 SQL까지 rollback되나?
이 조회는 왜 느린가?
동시에 수정하면 어떤 값이 남나?
```

## JDBC가 필요한 순간

일반적인 회원 CRUD는 JPA가 아주 잘 처리한다. 하지만 다음 상황에서는 SQL과 JDBC 수준의 이해가 다시 필요해진다.

- 실행 계획(`EXPLAIN`), 인덱스, 조인 순서를 보고 느린 조회를 고칠 때
- CTE, window function, 재귀 쿼리, DB 전용 함수처럼 복잡하거나 DB 종속적인 SQL을 쓸 때
- 통계·리포트·집계처럼 엔티티 하나를 조회하는 방식이 어색할 때
- 많은 데이터를 batch로 넣거나 수정하는 ETL·정산 작업을 할 때
- 비관적 락, 낙관적 락, 격리 수준, timeout을 직접 다뤄야 할 때
- JPA bulk update/delete 또는 native query를 사용해 영속성 컨텍스트와 실제 DB 상태가 어긋날 수 있을 때
- 장애 상황에서 connection pool, deadlock, lock wait, SQL 로그를 읽어야 할 때

JPA가 이런 일을 절대 못 하는 것은 아니다. `native query`, JPQL, JDBC 접근을 함께 사용할 수 있다. 다만 이때는 ORM의 자동화 범위를 벗어나므로 SQL, DB 제약조건, 트랜잭션을 이해한 사람이 결과를 책임져야 한다.

## AI가 코드를 작성해도 남는 개발자의 역할

AI는 CRUD 코드와 어노테이션을 빠르게 만들 수 있다. 하지만 다음 판단은 요구사항과 실제 데이터의 의미를 아는 사람이 해야 한다.

- 중복되면 안 되는 값은 무엇이며 DB 제약조건으로 어떻게 강제할 것인가?
- 여러 변경 중 무엇이 반드시 함께 성공하거나 함께 실패해야 하는가?
- 동시에 주문·재고·잔액을 바꾸면 어떤 결과가 정확한가?
- 어떤 조회에 인덱스가 필요하고, 비용이 커지는 이유는 무엇인가?
- ORM이 만든 SQL이 실제 서비스 트래픽에서도 괜찮은가?

코드를 만드는 능력보다 **데이터의 정합성과 실패 시 동작을 설계하고 검증하는 능력**이 백엔드 역할의 핵심이라는 생각으로 실습을 이어간다.

## 다음 실습에서 확인할 DB 질문

| 주제 | 순수 JDBC에서 직접 볼 것 | Spring/JPA에서 다시 볼 것 |
|---|---|---|
| 트랜잭션 경계 | 같은 Connection, auto commit, commit, rollback | `@Transactional`이 같은 Connection을 묶는 방식 |
| 동시성 | 두 Connection이 같은 행을 수정할 때의 결과 | 격리 수준, 낙관적/비관적 락 |
| 제약조건 | PK, UNIQUE, NOT NULL이 SQL을 막는 방식 | Entity 검증과 DB 제약조건의 역할 차이 |
| 조회 성능 | SQL, 인덱스, 실행 계획 | N+1, fetch join, projection, native query |
| 대량 변경 | `executeBatch()`와 영향받은 행 수 | JPA batch, bulk update와 persistence context |

## 돛단배 책과 연결해 볼 지점

[『Database System Concepts, Seventh Edition』](https://www.db-book.com/)의 장 순서와 현재 실습을 연결해 둔다. 책을 처음부터 다 읽고 다음으로 넘어갈 필요는 없다. Spring 프로젝트에서 실제 문제가 생길 때 이 표의 장으로 돌아오는 방식이 더 오래 남는다.

| 책의 장 | 지금 또는 다음 실습 | Spring/JPA 프로젝트에서 다시 만나는 장면 |
|---|---|---|
| Ch. 3 SQL | DDL, PK/UNIQUE, CRUD, PreparedStatement | JPQL·native query를 포함해 결국 DB가 받는 SQL 읽기 |
| Ch. 7 Relational Database Design | 회원 테이블의 키와 제약조건 | ERD, FK, 정규화, 애플리케이션 검증과 DB 제약조건의 역할 분리 |
| Ch. 9 Application Development | JDBC, `MemberDao`, SQL과 Java 객체 매핑 | DataSource, Repository, ORM 계층 |
| Ch. 14 Indexing | 나중에 데이터 양을 늘리고 `EXPLAIN` 보기 | 인덱스가 있는데도 느린 조회, 복합 인덱스의 컬럼 순서 |
| Ch. 15~16 Query Processing/Optimization | 같은 결과 SQL의 실행 계획과 비용 비교 | N+1, fetch join, projection, 페이지 조회, native query 선택 |
| Ch. 17 Transactions | 같은 Connection, auto commit, commit, rollback | `@Transactional`의 실제 경계 |
| Ch. 18 Concurrency Control | 두 Connection으로 같은 행 수정하기 | MVCC, 격리 수준, lost update, 낙관적/비관적 락 |
| Ch. 19 Recovery System | commit 이후의 지속성과 rollback의 의미 | 장애·deadlock·재시도와 데이터 정합성 |

바로 이어서 할 최소 실습은 Ch. 17이다. 그 뒤 월요일 Spring 프로젝트에 들어가고, 프로젝트에서 느린 조회나 동시성 문제가 실제로 보일 때 Ch. 14~19를 다시 펼친다. JDBC 연습을 끝없이 늘리기보다 실제 문제와 책의 개념을 왕복하는 편이 목적에 맞다.
