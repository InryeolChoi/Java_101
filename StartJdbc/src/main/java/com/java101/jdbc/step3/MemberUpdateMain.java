package com.java101.jdbc.step3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MemberUpdateMain {
    public static void main(String[] args) throws SQLException {
        int res = updateMember(999L, "yujin", "yujinahn@starship.com");
        System.out.println("update 결과 : " + res);
    }

    private static int updateMember(long id, String name, String email) throws SQLException {
        String url = "jdbc:h2:file:./StartJdbc/data/java101";
        String username = "sa";
        String password = "";
        String sql = "UPDATE members SET name = ?, email = ? WHERE id = ?";

        try (
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setLong(3, id);
            return pstmt.executeUpdate();
        }
    }
}
