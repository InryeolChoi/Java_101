package com.java101.jdbc.step6;

import com.java101.jdbc.step4.Member;
import com.java101.jdbc.step4.MemberDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MemberTransactionBoundaryMain {
    private static final String url = "jdbc:h2:file:./StartJdbc/data/java101";
    private static final String username = "sa";
    private static final String password = "";

    public static void main(String[] args) throws SQLException {
        cleanupTestMembers();
        autoCommitFailureExample();
        System.out.println(new MemberDao().findById(100L));

        cleanupTestMembers();
        rollbackFailureExample();
        System.out.println(new MemberDao().findById(100L));
    }

    private static void autoCommitFailureExample() throws SQLException {
        try (
            Connection conn = DriverManager.getConnection(url, username, password);
        ) {
            System.out.println("Autocommit 확인 : " + conn.getAutoCommit());
            insertMember(conn, new Member(
                    100L,
                    "auto-first",
                    "boundary-auto@example.com"));
            try {
                insertMember(conn, new Member(
                        101L,
                        "auto-second",
                        "boundary-auto@example.com"));
            } catch (SQLException e) {
                System.out.println("두번째 insert 실패");
            }
        }
    }

    private static void rollbackFailureExample() throws SQLException {
        try (
            Connection conn = DriverManager.getConnection(url, username, password);
        ) {
            conn.setAutoCommit(false);
            try {
                insertMember(conn, new Member(
                        100L,
                        "auto-first",
                        "boundary-auto@example.com"));

                insertMember(conn, new Member(
                        101L,
                        "auto-first",
                        "boundary-auto@example.com"));
                conn.commit();
            } catch (SQLException e) {
                System.out.println("실패");
                conn.rollback();
            }
        }
    }

    private static int insertMember (Connection conn,
                                      Member member) throws SQLException
    {
        String sql = "INSERT INTO members (id, name, email) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, member.getId());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getEmail());
            return pstmt.executeUpdate();
        }
    }

    private static void cleanupTestMembers() throws SQLException {
        String sql = "DELETE FROM members WHERE id IN (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, 100L);
            pstmt.setLong(2, 101L);
            pstmt.executeUpdate();
        }
    }
}
