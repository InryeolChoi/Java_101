# Connection과 Transaction

## 확인할 질문

- auto commit이 `true`라는 것은 무엇을 의미하는가?
- Java 메서드 하나가 자동으로 트랜잭션 하나가 되는가?
- 두 SQL을 하나의 트랜잭션으로 묶으려면 무엇이 같아야 하는가?
- 하나의 DB에 Connection을 두 개 연결할 수 있는가?
- Connection 1이 commit하지 않은 데이터가 Connection 2에서 보일까?

## 구현 체크리스트

- [x] auto commit 상태에서 INSERT 두 번 실행
- [x] 두 번째 INSERT를 UNIQUE 제약조건으로 실패시키기
- [x] 첫 번째 INSERT가 남는지 확인
- [x] `setAutoCommit(false)` 적용
- [x] 성공 시 `commit()`
- [x] 실패 시 `rollback()`
- [x] 새 Connection과 H2 Shell에서 commit/rollback 결과 확인

## 반드시 관찰할 조건

같은 트랜잭션으로 묶을 SQL은 같은 Connection에서 실행해야 한다.

```text
Connection 1 → SQL A → SQL B → commit 또는 rollback
```

## rollback을 직접 해보기

ID 6의 `liz` 이름을 `liz-rollback`으로 바꾼 뒤 바로 rollback하는 실험을 했다. `setAutoCommit(false)`를 호출한 Connection을 `updateMemberName(conn, ...)`에 그대로 전달했다. 이 메서드는 Connection을 새로 만들지 않고 PreparedStatement만 만들었다.

```text
autocommit 상태 : true
autocommit 상태 : false
update 결과 : 1
Member(id=6, name=liz, email=liz@xmail.com)
```

UPDATE의 반환값은 1이었다. SQL 자체는 실행됐지만 `conn.rollback()` 뒤 새 Connection으로 다시 조회하자 이름은 원래 `liz`였다. H2 Shell로 직접 조회해도 같은 결과였다.

```text
6 | liz | liz@xmail.com
```

여기서 Java 메서드가 트랜잭션을 정하는 것이 아니라 Connection의 `autoCommit` 상태와 `commit()` 또는 `rollback()` 호출이 트랜잭션을 정한다는 점을 확인했다.

## 실수하거나 헷갈린 부분

처음에는 조회를 위해 `new Member()`를 만들려 했다. `Member`는 데이터를 담는 객체이고 DB 조회는 `MemberDao`의 역할이라는 점을 다시 확인했다.

또 `updateMemberName()` 안에서 Connection을 새로 만들면 rollback 대상 Connection과 UPDATE Connection이 달라진다. 그래서 트랜잭션에 포함될 SQL 메서드에는 같은 Connection을 인자로 전달해야 한다.

## 같은 UPDATE라도 commit하면 남는다

같은 파일에서 rollback 실험 뒤 commit 실험도 이어서 실행했다.

```text
rollbackExample()
→ name = liz-rollback으로 UPDATE
→ rollback
→ 최종 조회에는 liz

commitExample()
→ name = liz-committed로 UPDATE
→ commit
→ 최종 조회에는 liz-committed
```

실행 뒤 `MemberDao`가 새 Connection으로 조회한 결과와 H2 Shell 결과가 모두 같았다.

```text
Member(id=6, name=liz-committed, email=liz@xmail.com)
6 | liz-committed | liz@xmail.com
```

같은 Java 파일에 두 메서드가 있다는 사실은 중요하지 않았다. 각 SQL이 어떤 Connection에서 실행됐는지가 commit과 rollback 결과를 결정했다.

## auto commit에서는 첫 성공이 이미 확정된다

ID 100과 101에 같은 email을 넣는 INSERT를 연달아 실행했다. email에는 UNIQUE 제약조건이 있으므로 두 번째 INSERT는 실패한다.

```text
Autocommit 확인 : true
첫 번째 INSERT: 성공
두 번째 INSERT: UNIQUE 제약조건 위반으로 실패
```

그런데 H2에서 ID 100을 조회하니 첫 번째 행은 남아 있었다.

```text
100 | auto-first | boundary-auto@example.com
```

`autoCommit=true`에서는 각 SQL 성공이 각각 commit된다. 따라서 뒤 SQL이 실패해도 앞 SQL을 되돌릴 수 없다. 다음에는 같은 실패를 `autoCommit=false`와 `rollback()`으로 실행해 ID 100도 남지 않게 만들어본다.

## Spring은 이것을 어떻게 처리할까?

- `@Transactional`은 트랜잭션 경계를 어떻게 만드는가?
- Spring은 같은 트랜잭션에서 같은 Connection을 어떻게 사용하게 하는가?
- commit과 rollback 호출은 누가 대신하는가?
