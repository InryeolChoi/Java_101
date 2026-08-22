package com.java101.jdbc.step3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MemberDeleteMain {
    public static void main(String[] args) throws SQLException {
        int affectedRows = deleteMember(999L);
        System.out.println("delete 결과 : " + affectedRows);
    }

    private static int deleteMember(long id) throws SQLException {
        String url = "jdbc:h2:file:./StartJdbc/data/java101";
        String username = "sa";
        String password = "";
        String sql = "DELETE FROM members WHERE id = ?";

        try (
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setLong(1, id);
            return pstmt.executeUpdate();
        }
    }
}
