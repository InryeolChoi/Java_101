# 03. PreparedStatement로 첫 INSERT

테이블까지 만들었으니 이번에는 members 테이블에 데이터를 한 건 넣어봤다.

Statement와 달리 PreparedStatement는 만들 때 SQL을 먼저 전달한다.

```sql
INSERT INTO members (id, name, email)
VALUES (?, ?, ?)
```

`?`에는 값을 따로 넣었다.

```java
pstmt.setInt(1, 001);
pstmt.setString(2, "peter");
pstmt.setString(3, "peter@xmail.com");
```

코드: [`MemberInsertMain.java`](../src/main/java/com/java101/jdbc/step3/MemberInsertMain.java)

## 실제로 저장됐나?

첫 실행에서는 `executeUpdate()`가 1을 반환했다. CREATE TABLE 때는 0이었는데, 이번에는 데이터 한 행을 추가했기 때문에 1이 나온 것이다.

H2 Shell에서 확인한 결과도 같았다.

```text
ID | NAME  | EMAIL
1  | peter | peter@xmail.com
(1 row)
```

PreparedStatement를 사용할 때는 `executeUpdate(sql)`이 아니라 `executeUpdate()`를 호출했다. SQL은 이미 `prepareStatement(sql)`을 호출할 때 전달했기 때문이다.

## 같은 코드를 한 번 더 실행해봤다

같은 ID로 다시 INSERT하자 다음 예외가 발생했다.

```text
JdbcSQLIntegrityConstraintViolationException
PRIMARY KEY ON PUBLIC.MEMBERS(ID)
```

두 번째 실행에서 데이터가 덮어써지는 것이 아니라 DB의 Primary Key 제약조건이 INSERT를 거부했다. 실패 후에도 기존 데이터는 그대로 한 행만 남아 있었다.

## ID만 바꾸면 email의 UNIQUE를 확인할 수 있을까?

ID를 2로 바꾸고 email은 기존 값인 `peter@xmail.com`을 그대로 넣어봤다. 이번에는 Primary Key가 겹치지 않으므로 email의 UNIQUE 제약조건에서 실패했다.

에러에 `UNIQUE`라고 단독으로 표시되지는 않아 처음에는 알아보기 어려웠다.

```text
Unique index or primary key violation
PUBLIC.CONSTRAINT_63
ON PUBLIC.MEMBERS(EMAIL)
VALUES ('peter@xmail.com')
```

H2는 UNIQUE 제약조건을 내부 unique index로 구현하고, 자동 생성한 이름인 `CONSTRAINT_63`과 `CONSTRAINT_INDEX_6`을 메시지에 보여줬다. 메타데이터를 조회해보니 의미가 더 분명했다.

```text
CONSTRAINT_NAME | CONSTRAINT_TYPE | COLUMN_NAME
CONSTRAINT_6    | PRIMARY KEY     | ID
CONSTRAINT_63   | UNIQUE          | EMAIL
```

즉 이번 예외는 email의 UNIQUE 제약조건이 정상적으로 INSERT를 막은 결과였다.

### 예외가 발생했는데 왜 BUILD SUCCESSFUL일까?

코드에서 `SQLException`을 catch한 뒤 `printStackTrace()`만 호출하고 예외를 다시 던지지 않았다. JVM 입장에서는 main 메서드가 정상적으로 끝났기 때문에 Gradle 작업도 성공으로 표시됐다.

```text
SQL 실행: 실패
Java 프로세스 종료 코드: 성공
Gradle task: BUILD SUCCESSFUL
```

따라서 `BUILD SUCCESSFUL`이 INSERT 성공을 뜻하는 것은 아니다. SQL 결과와 예외를 따로 확인해야 한다.

## 001이라고 쓰면 1 아닌가?

이번 코드에서 ID를 `001`로 작성했는데 결과는 1로 저장됐다. 다만 Java의 정수 리터럴은 앞에 0을 붙이면 8진수 문법으로 해석될 수 있다.

`001`은 우연히 10진수 1과 값이 같지만, `010`은 10진수 10이 아니라 8진수 10, 즉 10진수 8이 된다. `008`이나 `009`는 아예 컴파일되지 않는다.

members 테이블의 ID 타입도 `BIGINT`이므로 다음처럼 쓰는 편이 정확하다.

```java
pstmt.setLong(1, 1L);
```

## 이번 코드에서 확인한 것

- SQL 구조와 값을 `?`로 분리했다.
- `?`의 순서는 0이 아니라 1부터 시작했다.
- INSERT로 한 행이 추가되면 `executeUpdate()`가 1을 반환했다.
- 같은 Primary Key나 email을 다시 넣으면 `SQLException`이 발생했다.
- 실패하는 경우에도 PreparedStatement와 Connection을 각각 close하도록 작성했다.

변수 이름은 Java의 camelCase에 맞게 `affectedRows`로 수정했다.

## 근데 누가 데이터를 이런 식으로 하나씩 바꾸지?

제약조건을 확인하려고 소스 코드의 ID를 1에서 2로 직접 바꾸다 보니 이런 의문이 들었다. 실제 서비스에서 데이터를 넣을 때마다 Java 파일을 열어 값을 고치는 것은 당연히 아니다.

지금은 같은 프로그램을 통제된 값으로 반복 실행해보기 위한 실습이라 값을 코드에 직접 적었다. 실제 백엔드에서는 Controller가 HTTP 요청값을 받고, 그 값이 메서드 인자나 Member 객체로 Repository까지 전달된다.

```text
HTTP 요청
→ Controller
→ Service
→ Repository
→ PreparedStatement 값 바인딩
```

한 번에 많은 데이터를 저장해야 한다면 한 행씩 별도 요청하는 대신 JDBC batch의 `addBatch()`와 `executeBatch()` 같은 기능을 사용한다. 지금의 한 행 INSERT는 PreparedStatement의 기본 동작과 DB 제약조건을 확인하기 위한 작은 실험이었다.

## 그리고 try-catch가 너무 많다

Connection과 PreparedStatement를 각각 안전하게 닫으려고 하니 본래 목적이었던 INSERT보다 finally와 try-catch 코드가 더 눈에 띄었다.

이 시점부터는 Java의 `try-with-resources`를 사용해볼 수 있다.

```java
try (Connection conn = DriverManager.getConnection(url, username, password);
     PreparedStatement pstmt = conn.prepareStatement(sql)) {
    // 값 바인딩과 SQL 실행
} catch (SQLException e) {
    e.printStackTrace();
}
```

try 블록이 끝나면 선언의 역순으로 PreparedStatement와 Connection이 자동으로 close된다. Spring 기능이 아니라 Java의 `AutoCloseable` 문법이다.

수동 close 코드를 먼저 작성해봤기 때문에 try-with-resources가 단순한 문법 축약이 아니라, 예외가 발생해도 리소스를 정리하기 위한 장치라는 점이 더 잘 보였다.

이번에는 `main` 안에 있던 INSERT 코드를 `insertMember(id, name, email)` 메서드로 옮겼다. 처음에는 인자를 받도록 만들어놓고도 `setLong`, `setString`에는 예전 값을 그대로 적어놨다. 메서드 인자를 받는 것만으로는 부족하고, 실제 바인딩에도 그 변수를 사용해야 했다.

`catch`도 없애고 `throws SQLException`으로 넘겨봤다. 이미 저장된 ID 5를 다시 실행하자 예외가 `insertMember`에서 `main`, 마지막에는 JVM까지 올라갔고 프로그램도 실패로 끝났다. `throws`는 예외를 알아서 처리하는 문법이 아니라 현재 메서드에서 처리하지 않겠다는 뜻이었다.

H2에서 확인한 실제 데이터는 다음과 같았다.

```text
5 | winter | winter@xmail.com
```

## UPDATE도 executeUpdate였다

ID 1 회원의 이름과 email을 바꾸는 코드를 작성했다.

```sql
UPDATE members
SET name = ?, email = ?
WHERE id = ?
```

물음표의 순서대로 `name`, `email`, `id`를 바인딩해야 했다. INSERT와 SQL 모양은 다르지만 데이터를 변경하는 작업이라 똑같이 `executeUpdate()`를 사용했다.

```text
update 결과 : 1
```

반환값 1은 SQL을 실행했다는 뜻만이 아니라 실제로 영향받은 행이 한 개라는 뜻이었다. H2에서 확인하니 ID 1은 다음 값으로 바뀌어 있었다.

```text
1 | yujin | yujinahn@starship.com
```

## DELETE도 영향받은 행 수를 돌려준다

없는 ID 999를 삭제해봤다.

```sql
DELETE FROM members
WHERE id = ?
```

DELETE도 UPDATE와 마찬가지로 `executeUpdate()`를 사용했고 결과는 0이었다.

```text
delete 결과 : 0
```

SQL 문법이 틀린 것이 아니라 조건에 맞는 회원이 없어서 삭제된 행이 없다는 뜻이다. 실제로 존재하는 ID를 대상으로 실행하면 1이 반환되고, 다시 조회했을 때 그 행이 사라져야 한다.

ID 3의 `john`을 실제로 삭제한 뒤 전체 조회도 다시 해봤다. DELETE 결과는 1이었고, 목록에는 ID 1, 4, 5만 남았다. 이제 INSERT, SELECT, UPDATE, DELETE를 모두 JDBC로 직접 실행해본 셈이다.

## 남은 CRUD

- [x] Member INSERT
- [x] Member UPDATE
- [x] 존재하지 않는 ID DELETE: 0행
- [x] 존재하는 ID DELETE: 1행 및 재조회 확인
- [x] email UNIQUE 제약조건 위반 확인

CRUD를 모두 직접 작성한 뒤에는 조회 코드를 `MemberDao`로 모아보기 시작했다.

## Spring에서는 어떻게 달라질까?

Spring JDBC에서는 `JdbcTemplate.update()`가 PreparedStatement 생성, 실행, close 같은 반복 절차를 줄여준다. 하지만 INSERT SQL과 바인딩할 값 자체는 여전히 개발자가 정한다.
