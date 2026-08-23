package com.java101.jdbc.step5;

import com.java101.jdbc.step4.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MemberTransactionMain {
    private static final String url = "jdbc:h2:file:./StartJdbc/data/java101";
    private static final String username = "sa";
    private static final String password = "";

    public static void main(String[] args) throws SQLException {
        rollbackExample();
        commitExample();

        MemberDao memberDao = new MemberDao();
        Member member = memberDao.findById(6L);
        System.out.println(member);
    }

    private static void commitExample() throws SQLException {
        try (
            Connection conn = DriverManager.getConnection(url, username, password);
        ) {
            conn.setAutoCommit(false);
            updateMemberName(conn, 6L, "liz-committed");
            conn.commit();
        }
    }

    private static void rollbackExample() throws SQLException {
        try (
            Connection conn = DriverManager.getConnection(url, username, password);
        ) {
            System.out.println("autocommit 상태 : " + conn.getAutoCommit());
            conn.setAutoCommit(false);
            System.out.println("autocommit 상태 : " + conn.getAutoCommit());

            int affectedRows = updateMemberName(conn, 6L, "liz-rollback");
            System.out.println("update 결과 : " + affectedRows);
            conn.rollback();
        }
    }

    private static int updateMemberName(Connection conn,
                                         long id,
                                         String name) throws SQLException {
        String sql = "update members set name=? where id=?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);)
        {
            pstmt.setString(1, name);
            pstmt.setLong(2, id);
            return pstmt.executeUpdate();
        }
    }
}
