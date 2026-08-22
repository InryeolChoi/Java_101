# ResultSet과 객체 매핑

INSERT 다음에는 ID로 회원 한 명을 조회해봤다. 데이터를 바꾸는 SQL에서는 `executeUpdate()`를 썼지만, SELECT에서는 `executeQuery()`를 호출하고 `ResultSet`을 돌려받았다.

```sql
SELECT id, name, email
FROM members
WHERE id = ?
```

처음에는 `SELECT *`로 적었지만 실제로 읽을 컬럼을 SQL에 직접 적었다. ID는 테이블에서 `BIGINT`이므로 `getString()`보다 `getLong()`으로 꺼냈다.

```java
if (rs.next()) {
    System.out.println("id : " + rs.getLong("id"));
    System.out.println("name : " + rs.getString("name"));
    System.out.println("email : " + rs.getString("email"));
}
```

`ResultSet`을 받았다고 바로 첫 번째 행을 읽을 수 있는 것은 아니었다. 처음에는 첫 행 앞에 커서가 있고, `next()`를 호출해야 다음 행으로 이동한다. 행이 있으면 `true`, 없으면 `false`가 나온다.

ID 5를 조회했을 때 결과는 다음과 같았다.

```text
id : 5
name : winter
email : winter@xmail.com
```

없는 ID도 조회해보고 `false`인 경우에는 회원을 찾을 수 없다는 메시지가 나오도록 했다.

`ResultSet`도 JDBC 자원이어서 try-with-resources로 닫았다. 이 코드에서는 ResultSet이 먼저 닫히고, 그다음 PreparedStatement와 Connection이 닫힌다.

## getLong만 호출하면 안 되나?

처음에는 한 명만 조회해서 `rs.next()`를 한 번만 호출했다. 그래서 `getLong()`이 알아서 행을 읽는 것처럼 보이기도 했다. 하지만 `getLong()`은 현재 행의 컬럼만 읽고 커서를 움직이지 않는다.

전체 회원을 조회해보니 차이가 분명했다.

```java
while (rs.next()) {
    members.add(new Member(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email")
    ));
}
```

`next()`가 다음 행으로 이동하고, 행이 남아 있으면 `true`를 반환한다. 마지막 행을 지나면 `false`가 되어 반복이 끝난다. 조회한 네 행은 각각 직접 `Member` 객체로 만들어 `ArrayList`에 넣었다.

반환 타입은 구체적인 `ArrayList<Member>` 대신 `List<Member>`로 바꾸고, 조회 순서를 확실히 하려고 `ORDER BY id`도 추가했다. 실행 결과는 다음과 같았다.

```text
Member(id=1, name=peter, email=peter@xmail.com)
Member(id=3, name=john, email=john@xmail.com)
Member(id=4, name=karina, email=karina@xmail.com)
Member(id=5, name=winter, email=winter@xmail.com)
```

`Member`의 생성자, getter, `toString()`은 Lombok으로 만들었다. Lombok이 줄여준 것은 Java 객체의 반복 코드이고, ResultSet에서 컬럼을 꺼내 `Member` 생성자에 전달하는 JDBC 매핑은 직접 작성했다.

## 아, 이게 DAO였나?

`findMember()`와 `findAllMembers()`를 만들고 보니 예전에 봤던 DAO가 떠올랐다. SQL을 실행하고, ResultSet을 Java 객체로 바꿔서 호출자에게 돌려주는 부분이 바로 데이터 접근 코드였다.

UPDATE와 DELETE까지 작성한 뒤 `MemberDao`를 만들고, 조회 코드를 그 안으로 옮겼다. 이제 `MemberFindMain`과 `MemberFindAllMain`은 DAO를 호출해 결과를 출력만 하고, Connection, PreparedStatement, ResultSet, SQL은 `MemberDao` 안에 있다.

DAO가 JDBC를 없애준 것은 아니다. JDBC 코드를 한곳에 모아둔 것뿐이다. 그래서 다음 트랜잭션 단계에서 DAO 메서드 여러 개가 같은 Connection을 사용해야 하는 이유를 확인하기 좋다.

## DTO, VO, Entity는 아직 만들지 않기

Spring 프로젝트에서 DTO, VO, Entity라는 이름이 같이 나와서 잠깐 겁이 났다. 하지만 지금의 순수 JDBC 콘솔 실습에 모두 필요한 것은 아니다.

- DTO는 HTTP 요청과 응답처럼 계층 사이에서 데이터를 옮길 때 쓴다. 아직 Controller나 API가 없으므로 만들 이유가 없다.
- Entity는 JPA가 DB 테이블과 연결해 관리하는 객체다. JPA를 시작할 때 다룬다.
- VO는 `Email`, `Money`처럼 의미와 규칙을 가진 값 객체라는 뜻으로 나중에 배우면 된다. 자료마다 단순 데이터 객체라는 뜻으로도 섞어 써서 지금은 이름에 매달리지 않기로 했다.

현재 필요한 것은 `Member`와 `MemberDao`뿐이다.

## 구현 체크리스트

- [x] ID로 회원 한 명 조회
- [x] 전체 회원 조회
- [x] ResultSet에서 한 행과 여러 행 읽기
- [x] `Member` 객체로 수동 매핑
- [x] ResultSet, PreparedStatement, Connection 정리

## Spring과 JPA는 이것을 어떻게 처리할까?

- Spring JDBC의 `RowMapper`는 무엇을 대신하는가?
- JPA는 컬럼과 객체 필드의 관계를 어떻게 표현하는가?
- 직접 작성한 매핑 코드 중 어떤 부분이 사라지는가?
