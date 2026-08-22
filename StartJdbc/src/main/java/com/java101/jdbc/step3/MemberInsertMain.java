package com.java101.jdbc.step3;

import java.sql.*;

public class MemberInsertMain {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:h2:file:./StartJdbc/data/java101";
        String username = "sa";
        String password = "";
        String sql = "INSERT INTO members (id, name, email) VALUES (?, ?, ?)";

        try (
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement pstmt = conn.prepareStatement(sql);)
        {
            pstmt.setLong(1, 4L);
            pstmt.setString(2, "karina");
            pstmt.setString(3, "karina@xmail.com");
            int affectedRows = pstmt.executeUpdate();
            System.out.println("영향받은 row 수 : " + affectedRows);
        }
    }
}
