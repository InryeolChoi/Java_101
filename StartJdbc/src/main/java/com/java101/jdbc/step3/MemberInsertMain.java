package com.java101.jdbc.step3;

import java.sql.*;

public class MemberInsertMain {
    public static void main(String[] args) throws SQLException {
        int affectedRows = insertMember(5L, "winter", "winter@xmail.com");
        System.out.println("영향받은 행 수 :" + affectedRows);
    }

    private static int insertMember(long id, String name, String email) throws SQLException
    {
        String url = "jdbc:h2:file:./StartJdbc/data/java101";
        String username = "sa";
        String password = "";
        String sql = "INSERT INTO members (id, name, email) VALUES (?, ?, ?)";

        try (
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement pstmt = conn.prepareStatement(sql);)
        {
            pstmt.setLong(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            return (pstmt.executeUpdate());
        }
    }
}
