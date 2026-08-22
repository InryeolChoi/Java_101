# 01. JDBC로 DB에 연결해보기

JDBC 공부를 시작하면서 가장 먼저 해본 것은 DB에 직접 연결하는 것이었다.

Spring을 쓸 때는 DB 설정만 작성하면 알아서 연결됐는데, 순수 JDBC에서는 `Connection`을 직접 얻고 닫아야 한다. 이름만 보면 단순히 "연결" 같은데, 실제로 어디까지 담당하는 객체인지 궁금했다.

## 일단 연결부터 해봤다

DB는 별도 서버를 설치하지 않아도 되는 H2를 사용했다.

```groovy
runtimeOnly 'com.h2database:h2:2.4.240'
```

연결에 사용한 정보는 다음과 같다.

```text
URL      = jdbc:h2:file:./StartJdbc/data/java101
username = sa
password = 빈 문자열
```

코드: [`JdbcConnectionMain.java`](../src/main/java/com/java101/jdbc/step1/JdbcConnectionMain.java)

`DriverManager.getConnection()`으로 Connection을 얻은 뒤 구현체, auto commit, close 전후 상태를 출력했다.

```text
작업 디렉토리 : /Users/inchoi/pracJava
Connection 구현체 : org.h2.jdbc.JdbcConnection
close 전 isClosed : false
autocommit : true
close 후 isClosed : true
```

실행 후 `StartJdbc/data/java101.mv.db` 파일도 생성됐다. JDBC URL에 적은 상대 경로는 프로그램의 현재 작업 디렉터리를 기준으로 찾는다는 것도 알게 됐다.

## Connection은 정확히 무엇일까?

처음에는 DB 주소나 계정 정보를 담아두는 객체에 가까울 거라고 생각했다. 직접 사용해보니 Connection은 Java 프로그램과 DB 사이에 열린 하나의 세션에 더 가까웠다.

Connection을 통해 Statement를 만들고, auto commit을 설정하고, 나중에는 `commit()`과 `rollback()`도 실행한다. 결국 트랜잭션의 범위도 Connection을 기준으로 생각해야 한다.

그리고 코드에서는 `java.sql.Connection`을 사용했지만, 실제 객체는 `org.h2.jdbc.JdbcConnection`이었다. Java는 공통 JDBC 인터페이스를 제공하고 H2 Driver가 실제 구현을 제공하는 구조였다.

H2 클래스를 코드에서 직접 사용하지 않았는데도 Driver가 자동으로 발견됐다. 예전에 본 `Class.forName("org.h2.Driver")`도 이번 코드에는 필요하지 않았다.

## 하면서 실수한 것

처음에는 출력을 전부 `print()`로 작성해서 결과가 한 줄에 붙어 나왔다.

```text
/Users/inchoi/pracJavaorg.h2.jdbc.JdbcConnectionfalsetrue
```

출력 이름과 실제 값도 서로 맞지 않았다. 단순한 출력이라 생각했는데, 지금처럼 동작을 관찰하는 실습에서는 출력 자체가 검증 수단이라 이름을 정확하게 붙이는 것이 중요했다.

또 연결에 실패하면 `conn`이 `null`인데, 마지막에 바로 `conn.isClosed()`를 호출하려 했다. `SQLException`만 생각하고 그 다음에 발생할 수 있는 `NullPointerException`은 놓쳤다. close 전후 모두 null 여부를 생각해야 했다.

DB 파일을 제외할 때는 `.idea/.gitignore`와 저장소 루트의 `.gitignore`가 잠시 헷갈렸다. H2 DB 파일은 루트 `.gitignore`에 다음과 같이 추가했다.

```gitignore
/StartJdbc/data/
```

## 한 DB에 Connection을 두 개 연결할 수도 있나?

가능하다. 같은 DB URL로 `getConnection()`을 두 번 호출하면 서로 다른 Connection이 만들어진다.

각 Connection은 별도의 세션이므로 auto commit과 트랜잭션 상태도 따로 가진다. 나중에 다음 상황을 직접 확인해보고 싶다.

```text
Connection 1에서 INSERT
→ 아직 commit하지 않음
→ Connection 2에서 SELECT하면 보일까?
```

이 결과는 트랜잭션과 격리 수준을 공부할 때 확인할 예정이다.

## 그러면 Spring은 뭘 해주고 있었을까?

아직 Spring JDBC를 사용하지는 않았지만, 지금 단계에서 짐작되는 부분이 있다.

Spring에서는 보통 `DriverManager`를 직접 호출하기보다 `DataSource`에서 Connection을 얻는다. Spring Boot를 사용하면 HikariCP 같은 Connection Pool도 기본적으로 붙는다. 매번 물리적인 연결을 새로 만드는 대신 미리 준비한 연결을 빌려 쓰는 방식이다.

또 `JdbcTemplate`은 Connection이나 Statement를 닫는 반복 코드를 상당 부분 대신 처리한다. `@Transactional`을 사용했을 때 commit과 rollback이 자동으로 처리되는 것도 결국 내부에서는 Connection을 관리하기 때문일 것이다.

지금은 자동 처리를 사용하기 전에 Connection을 직접 열고 닫으면서 어떤 작업이 필요했는지 기억해두려고 한다.

## 다음에 확인할 것

Connection만 얻어서는 SQL을 실행할 수 없다. 다음에는 Connection에서 `Statement`를 만들고 실제 테이블을 생성해본다.
