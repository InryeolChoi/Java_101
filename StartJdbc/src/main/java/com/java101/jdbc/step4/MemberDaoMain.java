package com.java101.jdbc.step4;

import java.sql.SQLException;

public class MemberDaoMain {
    public static void main(String[] args) throws SQLException {
        MemberDao memberDao = new MemberDao();
        int affectedRows = memberDao.save(new Member(6L, "liz", "liz@xmail.com"));
        System.out.println("insert 결과 : " + affectedRows);
    }
}
