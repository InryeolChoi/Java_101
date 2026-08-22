# PreparedStatement와 CRUD

> 학습 예정 문서다. 실습 후 실제 코드, 결과, 실수를 기록한다.

## 확인할 질문

- `Statement`와 `PreparedStatement`는 무엇이 다른가?
- SQL의 `?`에는 값을 어떻게 바인딩하는가?
- `executeUpdate()` 반환값 `0`과 `1`은 각각 무엇을 뜻하는가?
- 문자열 연결로 SQL을 만들면 어떤 문제가 생기는가?
- 존재하지 않는 ID를 UPDATE 또는 DELETE하면 어떻게 되는가?

## 구현 체크리스트

- [ ] Member INSERT
- [ ] Member UPDATE
- [ ] Member DELETE
- [ ] 중복 email INSERT 실패 확인
- [ ] PreparedStatement와 Connection 직접 close

## 실행 결과

실습 후 기록한다.

## 실수하거나 헷갈린 부분

실습 후 기록한다.

## Spring은 이것을 어떻게 처리할까?

- `JdbcTemplate.update()`는 어떤 반복 코드를 없애는가?
- 이름 기반 파라미터는 어떻게 사용할 수 있는가?
- SQL 작성 책임은 그대로 남는가?
