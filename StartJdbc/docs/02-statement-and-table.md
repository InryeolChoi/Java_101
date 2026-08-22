# 02. Statement로 테이블 만들어보기

Connection으로 DB에 연결했으니 이번에는 SQL을 실행해봤다.

`Statement`라는 이름을 처음 봤을 때는 SQL 쿼리를 담는 클래스라고 생각했다. 직접 사용해보니 SQL을 보관하는 객체라기보다, Connection을 통해 SQL을 DB에 전달하고 실행하는 객체에 가까웠다.

```text
Connection
└── Statement
    └── SQL 실행
```

Statement도 혼자 만드는 것이 아니라 `conn.createStatement()`처럼 Connection에서 생성한다.

## members 테이블 생성

코드: [`MemberTableMain.java`](../src/main/java/com/java101/jdbc/step2/MemberTableMain.java)

아직 Member 객체나 CRUD는 만들지 않고 테이블 하나만 생성했다.

```sql
CREATE TABLE IF NOT EXISTS members (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
)
```

`IF NOT EXISTS`를 붙여서 프로그램을 여러 번 실행해도 이미 존재한다는 오류가 발생하지 않게 했다.

## executeUpdate인데 CREATE TABLE도 되네?

처음에는 `executeUpdate()`라는 이름 때문에 `UPDATE` SQL만 실행하는 메서드처럼 느껴졌다. 실제로는 데이터를 조회하지 않는 다음 SQL에 사용할 수 있었다.

- INSERT
- UPDATE
- DELETE
- CREATE TABLE 같은 DDL

실행 결과는 `0`이었다.

```text
영향받은 행 수 : 0
```

처음에는 성공 여부가 0이라는 뜻인가 싶었지만, 반환값은 성공과 실패가 아니라 영향을 받은 데이터 행 수다. 테이블을 만들었을 뿐 데이터 행을 변경한 것은 아니므로 0이 나오는 것이 정상이었다. SQL 실행이 실패하면 0으로 알려주는 것이 아니라 `SQLException`이 발생한다.

## 테이블이 진짜 생겼는지 터미널에서 확인

H2 Shell을 사용하면 파일 DB에 직접 접속할 수 있었다.

```bash
java -cp /Users/inchoi/.gradle/caches/modules-2/files-2.1/com.h2database/h2/2.4.240/686180ad33981ad943fdc0ab381e619b2c2fdfe5/h2-2.4.240.jar \
  org.h2.tools.Shell \
  -url jdbc:h2:file:./StartJdbc/data/java101 \
  -user sa \
  -password ''
```

접속해서 다음 SQL을 실행했다.

```sql
SHOW TABLES;
SELECT * FROM members;
```

확인한 결과:

```text
TABLE_NAME | TABLE_SCHEMA
MEMBERS    | PUBLIC

COLUMN_NAME | DATA_TYPE         | IS_NULLABLE
ID          | BIGINT            | NO
NAME        | CHARACTER VARYING | NO
EMAIL       | CHARACTER VARYING | NO
```

Primary Key와 Unique 제약조건도 생성돼 있었다. 아직 INSERT는 하지 않았기 때문에 조회 결과는 `0 rows`였다.

## 이번에는 Statement close를 빼먹었다

Connection 실습에서 Connection을 닫았으니 이번에는 Statement도 닫아야 했다. 처음 작성한 코드에서는 Connection만 닫고 Statement를 직접 닫지 않았다.

리소스는 만든 순서의 반대로 닫는다.

```text
Connection 생성
→ Statement 생성

Statement close
→ Connection close
```

Statement close를 추가하면서 다음 조건을 작성했다.

```java
if (stmt != null & conn != null)
```

하지만 이것도 다시 생각할 부분이 있다.

우선 boolean 조건에는 보통 `&`가 아니라 short-circuit 연산자인 `&&`를 사용한다. 더 중요한 문제는 Statement 생성에 실패했을 때다. Connection은 만들어졌지만 `stmt`가 null이면 조건 전체가 false가 되어 Connection까지 닫히지 않는다.

또 `stmt.close()`와 `conn.close()`를 같은 try 안에서 차례로 실행하면 Statement close에서 예외가 발생했을 때 Connection close까지 도달하지 못할 수 있다. 두 리소스가 각각 정리될 수 있게 작성해야 한다.

리소스가 하나 늘었을 뿐인데 null 검사와 예외 처리 코드가 눈에 띄게 늘었다. 순수 JDBC가 불편하다는 말을 조금씩 이해하기 시작했다.

## Connection과 Statement는 왜 둘 다 닫아야 할까?

처음에는 둘 다 파일 디스크립터를 열기 때문에 닫는 것인가 생각했다. 하지만 둘 다 반드시 각각 파일 디스크립터를 연다고 보기는 어려웠다.

Connection은 DB와의 연결과 세션을 나타낸다. 사용하는 DB에 따라 네트워크 소켓, 파일 핸들, 트랜잭션 상태 같은 자원을 붙잡을 수 있다. 지금 사용하는 H2 파일 DB에서는 파일과 잠금에 관련된 자원을 사용할 수 있고, MySQL이나 PostgreSQL처럼 별도 서버에 연결하면 보통 네트워크 소켓도 사용한다.

Statement는 DB 파일을 다시 여는 객체는 아니다. 이미 열린 Connection의 세션 안에서 SQL을 실행하기 위한 객체다. 실행 중에는 Driver나 DB가 SQL 실행 상태, 버퍼, 서버 측 Statement, ResultSet 같은 자원을 관리할 수 있다. 항상 특정 버퍼 하나를 가진다고 단정할 수는 없지만, DB나 Driver 쪽 자원을 점유할 수 있기 때문에 사용이 끝나면 닫아야 한다.

정리하면 다음처럼 이해할 수 있었다.

> Connection은 DB 세션과 실제 연결 자원을 관리하고, Statement는 그 세션에서 SQL을 실행하는 동안 필요한 JDBC/DB 자원을 사용한다. 둘 다 close해야 하지만 둘 다 각각 파일 디스크립터를 여는 것은 아니다.

## 그런데 DB 세션은 뭘까?

DB 세션은 애플리케이션과 DB가 연결된 동안 DB가 기억하고 있는 하나의 대화 상태라고 이해했다.

예를 들어 같은 DB에 두 Connection이 연결돼 있어도 DB는 두 연결을 구분해야 한다.

```text
Connection 1 → DB Session 1
Connection 2 → DB Session 2
```

각 세션에는 다음과 같은 상태가 연결될 수 있다.

- 누가 연결했는지에 대한 인증 정보
- 현재 진행 중인 트랜잭션
- auto commit과 트랜잭션 격리 수준
- 아직 commit되지 않은 변경 사항
- SQL 실행 중 만들어진 cursor나 임시 상태
- 세션이 보유한 lock

그래서 같은 DB를 바라보더라도 Connection 1에서 아직 commit하지 않은 작업과 Connection 2에서 보이는 상태가 다를 수 있다.

### 트랜잭션도 자원일까?

트랜잭션 자체는 파일 핸들이나 버퍼 같은 물리적 자원이라기보다 DB가 관리하는 논리적인 작업 단위이자 상태다.

다만 트랜잭션을 열어둔 동안에는 실제 DB 자원이 사용될 수 있다.

- 변경 내용을 되돌리기 위한 undo 또는 로그 정보
- 다른 트랜잭션과 충돌을 막기 위한 lock
- 일정한 조회 결과를 보여주기 위한 snapshot
- 아직 commit되지 않은 변경 상태

따라서 "Connection이 트랜잭션이라는 자원을 소유한다"기보다는 "Connection의 DB 세션이 현재 트랜잭션 상태를 유지하고, 그 트랜잭션 때문에 여러 DB 자원이 점유될 수 있다"고 표현하는 편이 더 정확하다.

Connection을 닫아야 하는 이유도 단순히 파일 하나를 닫기 위해서가 아니다. 세션과 트랜잭션 상태를 끝내고, 그 세션이 사용하던 실제 자원을 DB나 Connection Pool에 돌려주기 위해서다.

## Spring은 이걸 어떻게 처리할까?

Spring JDBC의 `JdbcTemplate`을 사용하면 Statement 생성과 실행, SQLException 처리, 리소스 정리 같은 반복 작업을 대신 처리해준다.

다만 SQL 자체가 없어지는 것은 아니다. Spring JDBC에서도 어떤 테이블을 조회하고 어떤 값을 저장할지는 SQL로 직접 작성한다. 반복적인 실행 절차는 줄여주지만 SQL과 DB에 대한 이해는 여전히 필요하다.

Spring Boot에는 `schema.sql`로 테이블을 초기화하는 기능도 있지만, 지금은 그 기능을 사용하지 않고 Java 코드가 DDL을 DB에 전달하는 과정을 직접 확인했다.

## 다음에 할 것

다음에는 `PreparedStatement`로 members 테이블에 첫 데이터를 INSERT한다.

그때 확인할 내용은 다음과 같다.

- SQL의 `?`에 값을 넣는 방법
- `Statement`와 `PreparedStatement`의 차이
- INSERT에서 `executeUpdate()`가 반환하는 값
- 문자열을 이어 붙여 SQL을 만드는 방식이 위험한 이유
