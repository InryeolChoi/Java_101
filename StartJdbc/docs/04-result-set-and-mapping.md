# ResultSet과 객체 매핑

> 학습 예정 문서다. 실습 후 실제 코드, 결과, 실수를 기록한다.

## 확인할 질문

- `executeQuery()`와 `executeUpdate()`는 어떻게 다른가?
- ResultSet은 처음에 어느 행을 가리키는가?
- `resultSet.next()`가 필요한 이유는 무엇인가?
- 조회 결과가 0개, 1개, 여러 개인 경우를 어떻게 처리할까?
- SQL 컬럼을 `Member` 객체로 누가 변환해야 할까?

## 구현 체크리스트

- [ ] ID로 회원 한 명 조회
- [ ] 전체 회원 조회
- [ ] ResultSet 순회
- [ ] `Member` 객체로 수동 매핑
- [ ] ResultSet, PreparedStatement, Connection 역순 close

## 실행 결과

실습 후 기록한다.

## 실수하거나 헷갈린 부분

실습 후 기록한다.

## Spring과 JPA는 이것을 어떻게 처리할까?

- Spring JDBC의 `RowMapper`는 무엇을 대신하는가?
- JPA는 컬럼과 객체 필드의 관계를 어떻게 표현하는가?
- 직접 작성한 매핑 코드 중 어떤 부분이 사라지는가?
