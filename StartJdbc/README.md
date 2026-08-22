# StartJdbc

## 목표
- Spring이나 ORM 없이 순수 JDBC로 CRUD를 구현한다.
- `Connection`, `PreparedStatement`, `ResultSet`의 역할을 이해한다.
- auto commit, `commit()`, `rollback()`을 직접 다루며 트랜잭션 경계를 확인한다.
- 이후 Spring JDBC와 JPA가 무엇을 추상화하는지 비교할 학습 기록을 남긴다.

## 실습 환경
- Java 21
- Gradle 9.7.1
- H2 2.4.240 파일 데이터베이스
- JDBC URL: `jdbc:h2:file:./StartJdbc/data/java101`

## 진행 상황

- [x] `Connection` 획득과 직접 close
- [x] `Statement`를 이용한 `members` 테이블 생성
- [ ] `PreparedStatement`를 이용한 CRUD
- [ ] `ResultSet`과 `Member` 수동 매핑
- [ ] auto commit, `commit()`, `rollback()` 실험
- [ ] 순수 JDBC 반복 코드 정리 및 Spring/JPA 비교

## 학습 기록

1. [Connection과 DB 연결](./docs/01-connection.md)
2. [Statement와 테이블 생성](./docs/02-statement-and-table.md)
3. [PreparedStatement와 CRUD](./docs/03-prepared-statement-crud.md)
4. [ResultSet과 객체 매핑](./docs/04-result-set-and-mapping.md)
5. [Connection과 Transaction](./docs/05-transaction.md)
6. [순수 JDBC의 불편함과 추상화](./docs/06-jdbc-pain-points.md)

완료한 실습 문서에는 실제 실행 결과와 실수를 기록한다. 아직 진행하지 않은 문서는 앞으로 확인할 질문과 체크리스트만 작성해 둔다.
