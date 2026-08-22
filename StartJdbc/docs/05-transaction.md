# Connection과 Transaction

> 학습 예정 문서다. 실습 후 실제 코드, 결과, 실수를 기록한다.

## 확인할 질문

- auto commit이 `true`라는 것은 무엇을 의미하는가?
- Java 메서드 하나가 자동으로 트랜잭션 하나가 되는가?
- 두 SQL을 하나의 트랜잭션으로 묶으려면 무엇이 같아야 하는가?
- 하나의 DB에 Connection을 두 개 연결할 수 있는가?
- Connection 1이 commit하지 않은 데이터가 Connection 2에서 보일까?

## 구현 체크리스트

- [ ] auto commit 상태에서 INSERT 두 번 실행
- [ ] 두 번째 INSERT를 UNIQUE 제약조건으로 실패시키기
- [ ] 첫 번째 INSERT가 남는지 확인
- [ ] `setAutoCommit(false)` 적용
- [ ] 성공 시 `commit()`
- [ ] 실패 시 `rollback()`
- [ ] 두 Connection 사이의 데이터 가시성 확인

## 반드시 관찰할 조건

같은 트랜잭션으로 묶을 SQL은 같은 Connection에서 실행해야 한다.

```text
Connection 1 → SQL A → SQL B → commit 또는 rollback
```

## 실행 결과

실습 후 기록한다.

## 실수하거나 헷갈린 부분

실습 후 기록한다.

## Spring은 이것을 어떻게 처리할까?

- `@Transactional`은 트랜잭션 경계를 어떻게 만드는가?
- Spring은 같은 트랜잭션에서 같은 Connection을 어떻게 사용하게 하는가?
- commit과 rollback 호출은 누가 대신하는가?
