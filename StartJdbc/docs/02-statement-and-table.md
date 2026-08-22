# 02. Statement로 테이블 만들어보기

Connection으로 DB에 연결했으니 이번에는 SQL을 실행해봤다.
`Statement`라는 이름을 처음 봤을 때는 SQL 쿼리를 담는 클래스라고 생각했다. 
직접 사용해보니 SQL을 보관하는 객체라기보다, Connection을 통해 SQL을 DB에 전달하고 실행하는 객체에 가까웠다.

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

처음에는 `executeUpdate()`라는 이름 때문에 `UPDATE` SQL만 실행하는 메서드처럼 느껴졌다. 
실제로는 데이터를 조회하지 않는 다음 SQL에 사용할 수 있었다.

- INSERT
- UPDATE
- DELETE
- CREATE TABLE 같은 DDL

실행 결과는 `0`이었다.

```text
영향받은 행 수 : 0
```

처음에는 성공 여부가 0이라는 뜻인가 싶었지만, 반환값은 성공과 실패가 아니라 영향을 받은 데이터 행 수다. 
테이블을 만들었을 뿐 데이터 행을 변경한 것은 아니므로 0이 나오는 것이 정상이었다. 
SQL 실행이 실패하면 0으로 알려주는 것이 아니라 `SQLException`이 발생한다.

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

Primary Key와 Unique 제약조건도 생성돼 있었다. 
아직 INSERT는 하지 않았기 때문에 조회 결과는 `0 rows`였다.

## 이번에는 Statement close를 빼먹었다

Connection 실습에서 Connection을 닫았으니 이번에는 Statement도 닫아야 했다. 
처음 작성한 코드에서는 Connection만 닫고 Statement를 직접 닫지 않았다.

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

우선 boolean 조건에는 보통 `&`가 아니라 short-circuit 연산자인 `&&`를 사용한다. 

더 중요한 문제는 Statement 생성에 실패했을 때다. 
Connection은 만들어졌지만 `stmt`가 null이면 조건 전체가 false가 되어 Connection까지 닫히지 않는다.

또 `stmt.close()`와 `conn.close()`를 같은 try 안에서 차례로 실행하면 
Statement close에서 예외가 발생했을 때 Connection close까지 도달하지 못할 수 있다. 

두 리소스가 각각 정리될 수 있게 작성해야 한다.

리소스가 하나 늘었을 뿐인데 null 검사와 예외 처리 코드가 눈에 띄게 늘었다. 
순수 JDBC가 불편하다는 말을 조금씩 이해하기 시작했다.
